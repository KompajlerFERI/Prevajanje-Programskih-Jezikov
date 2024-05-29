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
    DIVIDES,
    INTIGERDIVIDES,
    POW,
    PRINT,
    IN
}



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
    override val states = (1 .. 32).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 29, 30, 31, 32, 33)
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
        //{A..Za..z} / v, f, p
        for (i in 0 until 26) {
            if (i+97 == 112) continue // skip p
            if (i+97 == 102) continue // skip f
            if (i+97 == 105) continue // skip i
            setTransition(1, i+97, 13)
        }

        // in_____________________________________
        setTransition(1, 'i', 18)
        //i{A..Z}
        for (i in 0 until 26) {
            setTransition(18, i+65, 13)
        }
        //i{a..z} / o
        for (i in 0 until 26) {
            if (i+97 == 110) continue // skip n
            setTransition(18, i+97, 13)
        }
        //i{0..9}
        for (i in 0 until 10) {
            setTransition(18, i+48, 14)
        }

        setTransition(18, 'n', 19)
        //in{A..Z}
        for (i in 0 until 26) {
            setTransition(19, i+65, 13)
        }
        //in{a..z} / o
        for (i in 0 until 26) {
            setTransition(19, i+97, 13)
        }
        //in{0..9}
        for (i in 0 until 10) {
            setTransition(19, i+48, 14)
        }


        //______________________________________
        // for ______________________________________
        setTransition(1, 'f', 22)
        //f{A..Z}
        for (i in 0 until 26) {
            setTransition(22, i+65, 13)
        }
        //f{a..z} / o
        for (i in 0 until 26) {
            if (i+97 == 111) continue // skip o
            setTransition(22, i+97, 13)
        }
        //f{0..9}
        for (i in 0 until 10) {
            setTransition(22, i+48, 14)
        }

        setTransition(22, 'o', 23)
        //fo{A..Z}
        for (i in 0 until 26) {
            setTransition(23, i+65, 13)
        }
        //fo{a..z} / r
        for (i in 0 until 26) {
            if (i+97 == 114) continue // skip r
            setTransition(23, i+97, 13)
        }
        //fo{0..9}
        for (i in 0 until 10) {
            setTransition(23, i+48, 14)
        }

        setTransition(23, 'r', 24)
        //for{A..Z}
        for (i in 0 until 26) {
            setTransition(24, i+65, 13)
        }
        //for{a..z}
        for (i in 0 until 26) {
            if (i+97 == 101) continue // skip e
            setTransition(24, i+97, 13)
        }
        //for{0..9}
        for (i in 0 until 10) {
            setTransition(24, i+48, 14)
        }

        setTransition(24, 'e', 33)
        //fore{A..Z}
        for (i in 0 until 26) {
            setTransition(33, i+65, 13)
        }
        //fore{a..z}
        for (i in 0 until 26) {
            if (i+97 == 97) continue // skip a
            setTransition(33, i+97, 13)
        }
        //fore{0..9}
        for (i in 0 until 10) {
            setTransition(33, i+48, 14)
        }

        setTransition(33, 'a', 34)
        //forea{A..Z}
        for (i in 0 until 26) {
            setTransition(34, i+65, 13)
        }
        //forea{a..z}
        for (i in 0 until 26) {
            if (i+97 == 99) continue // skip c
            setTransition(34, i+97, 13)
        }
        //forea{0..9}
        for (i in 0 until 10) {
            setTransition(34, i+48, 14)
        }

        setTransition(34, 'c', 35)
        //foreac{A..Z}
        for (i in 0 until 26) {
            setTransition(35, i+65, 13)
        }
        //foreac{a..z}
        for (i in 0 until 26) {
            if (i+97 == 104) continue // skip h
            setTransition(35, i+97, 13)
        }
        //foreac{0..9}
        for (i in 0 until 10) {
            setTransition(35, i+48, 14)
        }

        setTransition(35, 'h', 36)
        //foreach{A..Z}
        for (i in 0 until 26) {
            setTransition(36, i+65, 13)
        }
        //foreach{a..z}
        for (i in 0 until 26) {
            setTransition(36, i+97, 13)
        }
        //foreach{0..9}
        for (i in 0 until 10) {
            setTransition(36, i+48, 14)
        }


        //______________________________________
        // print ______________________________________
        setTransition(1, 'p', 28)
        //p{A..Z}
        for (i in 0 until 26) {
            setTransition(28, i+65, 13)
        }
        //p{a..z} / o
        for (i in 0 until 26) {
            if (i+97 == 114) continue // skip r
            setTransition(28, i+97, 13)
        }
        //p{0..9}
        for (i in 0 until 10) {
            setTransition(28, i+48, 14)
        }

        setTransition(28, 'r', 29)
        //pr{A..Z}
        for (i in 0 until 26) {
            setTransition(29, i+65, 13)
        }
        //pr{a..z} / o
        for (i in 0 until 26) {
            if (i+97 == 105) continue // skip i
            setTransition(29, i+97, 13)
        }
        //pr{0..9}
        for (i in 0 until 10) {
            setTransition(29, i+48, 14)
        }

        setTransition(29, 'i', 30)
        //pri{A..Z}
        for (i in 0 until 26) {
            setTransition(30, i+65, 13)
        }
        //pri{a..z} / o
        for (i in 0 until 26) {
            if (i+97 == 110) continue // skip n
            setTransition(30, i+97, 13)
        }
        //pri{0..9}
        for (i in 0 until 10) {
            setTransition(30, i+48, 14)
        }

        setTransition(30, 'n', 31)
        //prin{A..Z}
        for (i in 0 until 26) {
            setTransition(31, i+65, 13)
        }
        //prin{a..z} / t
        for (i in 0 until 26) {
            if (i+97 == 116) continue // skip t
            setTransition(31, i+97, 13)
        }
        //prin{0..9}
        for (i in 0 until 10) {
            setTransition(31, i+48, 14)
        }

        setTransition(31, 't', 32)
        //print{A..Z}
        for (i in 0 until 26) {
            setTransition(32, i+65, 13)
        }
        //print{a..z}
        for (i in 0 until 26) {
            setTransition(32, i+97, 13)
        }
        //print{0..9}
        for (i in 0 until 10) {
            setTransition(32, i+48, 14)
        }

        //______________________________________
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

        setTransition(1, '=', 17)





        setTransition(1, ';', 21)

        //{A..Z}
        for (i in 0 until 26) {
            setTransition(24, i+65, 13)
        }
        //{A..Za..z}
        for (i in 0 until 26) {
            setTransition(24, i+97, 13)
        }
        //{0..9}
        for (i in 0 until 10) {
            setTransition(24, i+48, 14)
        }

        setTransition(1, ',', 25)
        setTransition(1, '{', 26)
        setTransition(1, '}', 27)



        setSymbol(2, Symbol.REAL)
        setSymbol(4, Symbol.REAL)
        setSymbol(5, Symbol.PLUS)
        setSymbol(6, Symbol.MINUS)
        setSymbol(7, Symbol.TIMES)
        setSymbol(8, Symbol.DIVIDES)
        setSymbol(9, Symbol.INTIGERDIVIDES)
        setSymbol(10, Symbol.POW)
        setSymbol(11, Symbol.LBRACKET)
        setSymbol(12, Symbol.RBRACKET)
        setSymbol(13, Symbol.VARIABLE)
        setSymbol(14, Symbol.VARIABLE)
        setSymbol(15, Symbol.SKIP)
        setSymbol(16, Symbol.EOF)

        setSymbol(17, Symbol.EQUALS)
        setSymbol(18, Symbol.VARIABLE)
        setSymbol(19, Symbol.VARIABLE)
        setSymbol(20, Symbol.DEFINE)
        setSymbol(21, Symbol.SEMICOL)
        setSymbol(22, Symbol.VARIABLE)
        setSymbol(23, Symbol.VARIABLE)
        setSymbol(33, Symbol.FOREACH)
        setSymbol(25, Symbol.COMMA)
        setSymbol(26, Symbol.BLOCK_START)
        setSymbol(27, Symbol.BLOCK_END)
        setSymbol(28, Symbol.VARIABLE)
        setSymbol(29, Symbol.VARIABLE)
        setSymbol(30, Symbol.VARIABLE)
        setSymbol(31, Symbol.VARIABLE)
        setSymbol(32, Symbol.PRINT)
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
            Symbol.LBRACKET -> {
                token = lex.getToken()
                val result = EXPR()
                if (token.symbol == Symbol.RBRACKET) {
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
