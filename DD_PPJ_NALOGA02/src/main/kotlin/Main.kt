import java.io.InputStream
import java.io.File

const val ERROR_STATE = 0

enum class Symbol {
    EOF,
    SKIP,
    CONST,
    VARIABLE,
    EQUALS,
    TYPE_NUMBER,
    TYPE_STRING,
    TYPE_COORDINATE,
    TYPE_LIST,
    LTHAN,
    MTHAN,
    CITY,
    BLOCK_START,
    BLOCK_END,
    NAME,
    COLON,
    STRING,
    SHAPE,
    LINE,
    LSQ_BRACKET,
    COMMA,
    RSQ_BRACKET,
    LBRACKET,
    RBRACKET,
    MARKER,
    POINT,
    ROUTES,
    ROAD,
    BEND,
    FOREACH,
    HIGHLIGHT,
    SEMICOL,
    HASH,
    REAL,
    PLUS,
    MINUS,
    TIMES,
    DIVIDE,
    INTEGER_DIVIDE,
    POW,
    PRINT,
    IN
};


const val EOF = -1
const val NEWLINE = '\n'.code

interface DFA {
    val states: Set<Int>
    val alphabet: IntRange
    fun next(state: Int, code: Int): Int
    fun symbol(state: Int): Symbol
    val startState: Int
    val finalStates: Set<Int>
}

object ForForeachFFFAutomaton: DFA {
    override val states = (1 .. 16).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
    private val numberOfStates = states.max() + 1 // plus the ERROR_STATE
    private val numberOfCodes = alphabet.max() + 1 // plus the EOF
    private val transitions = Array(numberOfStates) {IntArray(numberOfCodes)}
    private val values = Array(numberOfStates) {Symbol.SKIP}

    private fun setTransition(from: Int, chr: Char, to: Int) {
        transitions[from][chr.code + 1] = to // + 1 because EOF is -1 and the array starts at 0
    }

    private fun setTransition(from: Int, code: Int, to: Int) {
        transitions[from][code + 1] = to
    }

    private fun setSymbol(state: Int, symbol: Symbol) {
        values[state] = symbol
    }

    override fun next(state: Int, code: Int): Int {
        assert(states.contains(state))
        assert(alphabet.contains(code))
        return transitions[state][code + 1]
    }

    override fun symbol(state: Int): Symbol {
        assert(states.contains(state))
        return values[state]
    }
    init {
        //{0..9}
        for (i in 0 until 10) {
            setTransition(1, i+48, 2)
        }
        //{0..9}+
        for (i in 0 until 10) {
            setTransition(2, i+48, 2)
        }
        //{0..9}+.
        for (i in 0 until 10) {
            setTransition(2, '.', 3)
        }
        //{0..9}+.{0..9}
        for (i in 0 until 10) {
            setTransition(3, i+48, 4)
        }
        //{0..9}+.{0..9}+
        for (i in 0 until 10) {
            setTransition(4, i+48, 4)
        }

        //+
        setTransition(1, '+', 5)
        //-
        setTransition(1, '-', 6)
        //*
        setTransition(1, '*', 7)
        // /
        setTransition(1, '/', 8)
        // //
        setTransition(8, '/', 9)
        // ^
        setTransition(1, '^', 10)
        // (
        setTransition(1, '(', 11)
        // )
        setTransition(1, ')', 12)

        //{A..Z}
        for (i in 0 until 26) {
            setTransition(1, i+65, 13)
        }
        //{A..Za..z}
        for (i in 0 until 26) {
            setTransition(1, i+97, 13)
        }
        //{A..Z}+
        for (i in 0 until 26) {
            setTransition(13, i+65, 13)
        }
        //{A..Za..z}+
        for (i in 0 until 26) {
            setTransition(13, i+97, 13)
        }
        //{A..Za..z}+{0..9}
        for (i in 0 until 10) {
            setTransition(13, i+48, 14)
        }
        //{A..Za..z}+{0..9}+
        for (i in 0 until 10) {
            setTransition(14, i+48, 14)
        }
        // \r
        setTransition(1, '\r', 15)
        // \t
        setTransition(1, '\t', 15)
        // \n
        setTransition(1, '\n', 15)
        // ' '
        setTransition(1, ' ', 15)
        //EOF
        setTransition(1, EOF, 16)

        setSymbol(2, Symbol.REAL)
        setSymbol(4, Symbol.REAL)
        setSymbol(5, Symbol.PLUS)
        setSymbol(6, Symbol.MINUS)
        setSymbol(7, Symbol.TIMES)
        setSymbol(8, Symbol.DIVIDES)
        setSymbol(9, Symbol.INTIGERDIVIDES)
        setSymbol(10, Symbol.POW)
        setSymbol(11, Symbol.LPAREN)
        setSymbol(12, Symbol.RPAREN)
        setSymbol(13, Symbol.VARIABLE)
        setSymbol(14, Symbol.VARIABLE)
        setSymbol(15, Symbol.SKIP)
        setSymbol(16, Symbol.EOF)
    }
}

data class Token(val symbol: Symbol, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: DFA, private val stream: InputStream) {
    private var last: Int? = null
    private var row = 1
    private var column = 1

    private fun updatePosition(code: Int) {
        if (code == NEWLINE) {
            row += 1
            column = 1
        } else {
            column += 1
        }
    }

    fun getToken(): Token {
        val startRow = row
        val startColumn = column
        val buffer = mutableListOf<Char>()

        var code = last ?: stream.read()
        var state = automaton.startState
        while (true) {
            val nextState = automaton.next(state, code)
            if (nextState == ERROR_STATE) break // Longest match

            state = nextState
            updatePosition(code)
            buffer.add(code.toChar())
            code = stream.read()
        }
        last = code // The code following the current lexeme is the first code of the next lexeme

        if (automaton.finalStates.contains(state)) {
            val symbol = automaton.symbol(state)
            return if (symbol == Symbol.SKIP) {
                getToken()
            } else {
                val lexeme = String(buffer.toCharArray())
                Token(symbol, lexeme, startRow, startColumn)
            }
        } else {
            throw Error("Invalid pattern at ${row}:${column}")
        }
    }
}

class Parser(private val lex: Scanner) {
    private var token = lex.getToken()

    fun EXPR(): Boolean {
        return ADDITIVE()
    }

    fun ADDITIVE(): Boolean {
        return MULTIPlicative() && ADDITIVE_PRIME()
    }

    fun ADDITIVE_PRIME(): Boolean {
        if (token.symbol == Symbol.PLUS || token.symbol == Symbol.MINUS) {
            token = lex.getToken()
            return MULTIPlicative() && ADDITIVE_PRIME()
        }
        return true
    }

    fun MULTIPlicative(): Boolean {
        return EXPONENTIAL() && MULTIPlicative_PRIME()
    }

    fun MULTIPlicative_PRIME(): Boolean {
        if (token.symbol == Symbol.TIMES || token.symbol == Symbol.DIVIDES || token.symbol == Symbol.INTIGERDIVIDES) {
            token = lex.getToken()
            return EXPONENTIAL() && MULTIPlicative_PRIME()
        }
        return true
    }

    fun EXPONENTIAL(): Boolean {
        return UNARY() && EXPONENTIAL_PRIME()
    }

    fun EXPONENTIAL_PRIME(): Boolean {
        if (token.symbol == Symbol.POW) {
            token = lex.getToken()
            return UNARY() && EXPONENTIAL_PRIME()
        }
        return true
    }

    fun UNARY(): Boolean {
        if (token.symbol == Symbol.PLUS || token.symbol == Symbol.MINUS) {
            token = lex.getToken()
            return PRIMARY()
        } else {
            return PRIMARY()
        }
    }

    fun PRIMARY(): Boolean {
        return when (token.symbol) {
            Symbol.REAL -> {
                token = lex.getToken()
                true
            }
            Symbol.VARIABLE -> {
                token = lex.getToken()
                true
            }
            Symbol.LPAREN -> {
                token = lex.getToken()
                val result = EXPR()
                if (token.symbol == Symbol.RPAREN) {
                    token = lex.getToken()
                    result
                } else {
                    false
                }
            }
            else -> false
        }
    }

    fun PARSE(): Boolean {
        val result = EXPR()
        return result && token.symbol == Symbol.EOF
    }
}


fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: <input_file_name> <output_file_name>")
        return
    }

    val fileInputName = args[0]
    val fileOutputName = args[1]
    val file = File(fileInputName)
    val fileOutput = File(fileOutputName)

    val lex = Scanner(ForForeachFFFAutomaton, file.inputStream())
    val parser = Parser(lex)

    val result = parser.PARSE()
    fileOutput.writeText(if (result) "accept" else "reject")
}
