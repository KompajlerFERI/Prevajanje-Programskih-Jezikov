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
    override val finalStates = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27)
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

        // <
        setTransition(1, '<', 5)
        // >
        setTransition(1, '>', 6)
        // {
        setTransition(1, '{', 7)
        // }
        setTransition(1, '}', 8)
        // :
        setTransition(1, ':', 9)
        // [
        setTransition(1, '[', 10)
        // ,
        setTransition(1, ',', 11)
        // ]
        setTransition(1, ']', 12)
        // (
        setTransition(1, '(', 13)
        // )
        setTransition(1, ')', 14)
        // ;
        setTransition(1, ';', 15)
        // #
        setTransition(1, '#', 16)
        // +
        setTransition(1, '+', 17)
        // -
        setTransition(8, '-', 18)
        // *
        setTransition(1, '*', 19)
        // /
        setTransition(1, '/', 20)
        // //
        setTransition(20, '/', 21)
        // ^
        setTransition(1, '^', 22)
        // \t
        setTransition(1, '\t', 23)
        // \n
        setTransition(1, '\n', 24)
        // ' '
        setTransition(1, ' ', 25)
        // \r
        setTransition(1, '\r', 26)
        // EOF
        setTransition(1, EOF, 27)



        //{A..Z}
        for (i in 0 until 26) {
            setTransition(1, i+65, 28)
        }
        //{A..Za..z} / l, c, n, s, m, p, r, b, f, h, i
        for (i in 0 until 26) {
            if (i+97 == 108) continue // skip l
            if (i+97 == 99) continue // skip c
            if (i+97 == 110) continue // skip n
            if (i+97 == 115) continue // skip s
            if (i+97 == 109) continue // skip m
            if (i+97 == 112) continue // skip p
            if (i+97 == 114) continue // skip r
            if (i+97 == 98) continue // skip b
            if (i+97 == 102) continue // skip f
            if (i+97 == 104) continue // skip h
            if (i+97 == 105) continue // skip i
            setTransition(1, i+97, 28)
        }
        //{A..Za..z_}
        setTransition(1, '_', 29)

        // list_____________________________________
            setTransition(1, 'c', 30)
                //c{A..Z}
                for (i in 0 until 26) {
                    setTransition(30, i+65, 28)
                }
                //c{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(30, i+97, 28)
                }
                //c{0..9}
                for (i in 0 until 10) {
                    setTransition(30, i+48, 29)
                }

            setTransition(30, 'o', 31)
                //co{A..Z}
                for (i in 0 until 26) {
                    setTransition(31, i+65, 28)
                }
                //co{a..z} / n
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(31, i+97, 28)
                }
                //co{0..9}
                for (i in 0 until 10) {
                    setTransition(31, i+48, 29)
                }

            setTransition(31, 'n', 32)
                //con{A..Z}
                for (i in 0 until 26) {
                    setTransition(32, i+65, 28)
                }
                //con{a..z} / s
                for (i in 0 until 26) {
                    if (i+97 == 115) continue // skip s
                    setTransition(32, i+97, 28)
                }
                //con{0..9}
                for (i in 0 until 10) {
                    setTransition(32, i+48, 29)
                }

            setTransition(32, 's', 33)
                //cons{A..Z}
                for (i in 0 until 26) {
                    setTransition(33, i+65, 28)
                }
                //cons{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(33, i+97, 28)
                }
                //const{0..9}
                for (i in 0 until 10) {
                    setTransition(33, i+48, 29)
                }

            setTransition(33, 't', 34)
                //const{A..Z}
                for (i in 0 until 26) {
                    setTransition(34, i+65, 28)
                }
                //const{a..z}
                for (i in 0 until 26) {
                    setTransition(34, i+97, 28)
                }
                //const{0..9}
                for (i in 0 until 10) {
                    setTransition(34, i+48, 29)
                }

        //num______________________________________
            setTransition(1, 'n', 35)
                //n{A..Z}
                for (i in 0 until 26) {
                    setTransition(35, i+65, 28)
                }
                //n{a..z} / u
                for (i in 0 until 26) {
                    if (i+97 == 117) continue // skip u
                    setTransition(35, i+97, 28)
                }
                //n{0..9}
                for (i in 0 until 10) {
                    setTransition(35, i+48, 29)
                }

            setTransition(35, 'u', 36)
                //nu{A..Z}
                for (i in 0 until 26) {
                    setTransition(36, i+65, 28)
                }
                //nu{a..z} / m
                for (i in 0 until 26) {
                    if (i+97 == 109) continue // skip m
                    setTransition(36, i+97, 28)
                }
                //nu{0..9}
                for (i in 0 until 10) {
                    setTransition(36, i+48, 29)
                }

            setTransition(36, 'm', 37)
                //num{A..Z}
                for (i in 0 until 26) {
                    setTransition(37, i+65, 28)
                }
                //num{a..z}
                for (i in 0 until 26) {
                    setTransition(37, i+97, 28)
                }
                //num{0..9}
                for (i in 0 until 10) {
                    setTransition(37, i+48, 29)
                }

        //string______________________________________
            setTransition(1, 's', 38)
                //s{A..Z}
                for (i in 0 until 26) {
                    setTransition(38, i+65, 28)
                }
                //s{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(38, i+97, 28)
                }
                //s{0..9}
                for (i in 0 until 10) {
                    setTransition(38, i+48, 29)
                }

            setTransition(38, 't', 39)
                //st{A..Z}
                for (i in 0 until 26) {
                    setTransition(39, i+65, 28)
                }
                //st{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(39, i+97, 28)
                }
                //st{0..9}
                for (i in 0 until 10) {
                    setTransition(39, i+48, 29)
                }

            setTransition(39, 'r', 40)
                //str{A..Z}
                for (i in 0 until 26) {
                    setTransition(40, i+65, 28)
                }
                //str{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(40, i+97, 28)
                }
                //str{0..9}
                for (i in 0 until 10) {
                    setTransition(40, i+48, 29)
                }

            setTransition(40, 'i', 41)
                //stri{A..Z}
                for (i in 0 until 26) {
                    setTransition(41, i+65, 28)
                }
                //stri{a..z} / n
                for (i in 0 until 26) {
                    if (i + 97 == 110) continue // skip n
                    setTransition(41, i + 97, 28)
                }
                //stri{0..9}
                for (i in 0 until 10) {
                    setTransition(41, i + 48, 29)
                }

            setTransition(41, 'n', 42)
                //strin{A..Z}
                for (i in 0 until 26) {
                    setTransition(42, i+65, 28)
                }
                //strin{a..z} / g
                for (i in 0 until 26) {
                    if (i + 97 == 103) continue // skip g
                    setTransition(42, i + 97, 28)
                }
                //strin{0..9}
                for (i in 0 until 10) {
                    setTransition(42, i + 48, 29)
                }

            setTransition(42, 'g', 43)
                //string{A..Z}
                for (i in 0 until 26) {
                    setTransition(43, i+65, 28)
                }
                //string{a..z}
                for (i in 0 until 26) {
                    setTransition(43, i+97, 28)
                }
                //string{0..9}
                for (i in 0 until 10) {
                    setTransition(43, i+48, 29)
                }

        //coord______________________________________
            setTransition(1, 'c', 44)
                //c{A..Z}
                for (i in 0 until 26) {
                    setTransition(44, i+65, 28)
                }
                //c{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(44, i+97, 28)
                }
                //c{0..9}
                for (i in 0 until 10) {
                    setTransition(44, i+48, 29)
                }

            setTransition(44, 'o', 45)
                //co{A..Z}
                for (i in 0 until 26) {
                    setTransition(45, i+65, 28)
                }
                //co{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(45, i+97, 28)
                }
                //co{0..9}
                for (i in 0 until 10) {
                    setTransition(45, i+48, 29)
                }

            setTransition(45, 'o', 46)
                //coo{A..Z}
                for (i in 0 until 26) {
                    setTransition(46, i+65, 28)
                }
                //coo{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(46, i+97, 28)
                }
                //coo{0..9}
                for (i in 0 until 10) {
                    setTransition(46, i+48, 29)
                }

            setTransition(46, 'r', 47)
                //coor{A..Z}
                for (i in 0 until 26) {
                    setTransition(47, i+65, 28)
                }
                //coor{a..z} / d
                for (i in 0 until 26) {
                    if (i+97 == 100) continue // skip d
                    setTransition(47, i+97, 28)
                }
                //coor{0..9}
                for (i in 0 until 10) {
                    setTransition(47, i+48, 29)
                }

            setTransition(47, 'd', 48)
                //coord{A..Z}
                for (i in 0 until 26) {
                    setTransition(48, i+65, 28)
                }
                //coord{a..z}
                for (i in 0 until 26) {
                    setTransition(48, i+97, 28)
                }
                //coord{0..9}
                for (i in 0 until 10) {
                    setTransition(48, i+48, 29)
                }

        // list_____________________________________
            setTransition(1, 'l', 49)
                //l{A..Z}
                for (i in 0 until 26) {
                    setTransition(49, i+65, 28)
                }
                //l{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip i
                    setTransition(49, i+97, 28)
                }
                //l{0..9}
                for (i in 0 until 10) {
                    setTransition(49, i+48, 29)
                }

            setTransition(49, 'i', 50)
                //li{A..Z}
                for (i in 0 until 26) {
                    setTransition(50, i+65, 28)
                }
                //li{a..z} / s
                for (i in 0 until 26) {
                    if (i+97 == 115) continue // skip s
                    setTransition(50, i+97, 28)
                }
                //li{0..9}
                for (i in 0 until 10) {
                    setTransition(50, i+48, 29)
                }

            setTransition(50, 's', 51)
                //lis{A..Z}
                for (i in 0 until 26) {
                    setTransition(51, i+65, 28)
                }
                //lis{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(51, i+97, 28)
                }
                //lis{0..9}
                for (i in 0 until 10) {
                    setTransition(51, i+48, 29)
                }

            setTransition(51, 't', 52)
                //list{A..Z}
                for (i in 0 until 26) {
                    setTransition(52, i+65, 28)
                }
                //list{a..z} / t
                for (i in 0 until 26) {
                    setTransition(52, i+97, 28)
                }
                //list{0..9}
                for (i in 0 until 10) {
                    setTransition(52, i+48, 29)
                }

        //city______________________________________
            setTransition(1, 'c', 53)
                //c{A..Z}
                for (i in 0 until 26) {
                    setTransition(53, i+65, 28)
                }
                //c{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(53, i+97, 28)
                }
                //c{0..9}
                for (i in 0 until 10) {
                    setTransition(53, i+48, 29)
                }

            setTransition(53, 'i', 54)
                //ci{A..Z}
                for (i in 0 until 26) {
                    setTransition(54, i+65, 28)
                }
                //ci{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(54, i+97, 28)
                }
                //ci{0..9}
                for (i in 0 until 10) {
                    setTransition(54, i+48, 29)
                }

            setTransition(54, 't', 55)
                //cit{A..Z}
                for (i in 0 until 26) {
                    setTransition(55, i+65, 28)
                }
                //cit{a..z} / y
                for (i in 0 until 26) {
                    if (i+97 == 121) continue // skip y
                    setTransition(55, i+97, 28)
                }
                //cit{0..9}
                for (i in 0 until 10) {
                    setTransition(55, i+48, 29)
                }

            setTransition(55, 'y', 56)
                //city{A..Z}
                for (i in 0 until 26) {
                    setTransition(56, i+65, 28)
                }
                //city{a..z}
                for (i in 0 until 26) {
                    setTransition(56, i+97, 28)
                }
                //city{0..9}
                for (i in 0 until 10) {
                    setTransition(56, i+48, 29)
                }

        //name______________________________________
            setTransition(1, 'n', 57)
                //n{A..Z}
                for (i in 0 until 26) {
                    setTransition(57, i+65, 28)
                }
                //n{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(57, i+97, 28)
                }
                //n{0..9}
                for (i in 0 until 10) {
                    setTransition(57, i+48, 29)
                }

            setTransition(57, 'a', 58)
                //na{A..Z}
                for (i in 0 until 26) {
                    setTransition(58, i+65, 28)
                }
                //na{a..z} / m
                for (i in 0 until 26) {
                    if (i+97 == 109) continue // skip m
                    setTransition(58, i+97, 28)
                }
                //na{0..9}
                for (i in 0 until 10) {
                    setTransition(58, i+48, 29)
                }

            setTransition(58, 'm', 59)
                //nam{A..Z}
                for (i in 0 until 26) {
                    setTransition(59, i+65, 28)
                }
                //nam{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(59, i+97, 28)
                }
                //nam{0..9}
                for (i in 0 until 10) {
                    setTransition(59, i+48, 29)
                }

            setTransition(59, 'e', 60)
                //name{A..Z}
                for (i in 0 until 26) {
                    setTransition(60, i+65, 28)
                }
                //name{a..z}
                for (i in 0 until 26) {
                    setTransition(60, i+97, 28)
                }
                //name{0..9}
                for (i in 0 until 10) {
                    setTransition(60, i+48, 29)
                }

        //shape______________________________________
            setTransition(1, 's', 61)
                //s{A..Z}
                for (i in 0 until 26) {
                    setTransition(61, i+65, 28)
                }
                //s{a..z} / h
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(61, i+97, 28)
                }
                //s{0..9}
                for (i in 0 until 10) {
                    setTransition(61, i+48, 29)
                }

            setTransition(61, 'h', 62)
                //sh{A..Z}
                for (i in 0 until 26) {
                    setTransition(62, i+65, 28)
                }
                //sh{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(62, i+97, 28)
                }
                //sh{0..9}
                for (i in 0 until 10) {
                    setTransition(62, i+48, 29)
                }

            setTransition(62, 'a', 63)
                //sha{A..Z}
                for (i in 0 until 26) {
                    setTransition(63, i+65, 28)
                }
                //sha{a..z} / p
                for (i in 0 until 26) {
                    if (i+97 == 112) continue // skip p
                    setTransition(63, i+97, 28)
                }
                //sha{0..9}
                for (i in 0 until 10) {
                    setTransition(63, i+48, 29)
                }

            setTransition(63, 'p', 64)
                //shap{A..Z}
                for (i in 0 until 26) {
                    setTransition(64, i+65, 28)
                }
                //shap{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(64, i+97, 28)
                }
                //shap{0..9}
                for (i in 0 until 10) {
                    setTransition(64, i+48, 29)
                }

            setTransition(64, 'e', 65)
                //shape{A..Z}
                for (i in 0 until 26) {
                    setTransition(65, i+65, 28)
                }
                //shape{a..z}
                for (i in 0 until 26) {
                    setTransition(65, i+97, 28)
                }
                //shape{0..9}
                for (i in 0 until 10) {
                    setTransition(65, i+48, 29)
                }

        //line______________________________________
            setTransition(1, 'l', 66)
                //l{A..Z}
                for (i in 0 until 26) {
                    setTransition(66, i+65, 28)
                }
                //l{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(66, i+97, 28)
                }
                //l{0..9}
                for (i in 0 until 10) {
                    setTransition(66, i+48, 29)
                }

            setTransition(66, 'i', 67)
                //li{A..Z}
                for (i in 0 until 26) {
                    setTransition(67, i+65, 28)
                }
                //li{a..z} / n
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(67, i+97, 28)
                }
                //li{0..9}
                for (i in 0 until 10) {
                    setTransition(67, i+48, 29)
                }

            setTransition(67, 'n', 68)
                //lin{A..Z}
                for (i in 0 until 26) {
                    setTransition(68, i+65, 28)
                }
                //lin{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(68, i+97, 28)
                }
                //lin{0..9}
                for (i in 0 until 10) {
                    setTransition(68, i+48, 29)
                }

            setTransition(68, 'e', 69)
                //line{A..Z}
                for (i in 0 until 26) {
                    setTransition(69, i+65, 28)
                }
                //line{a..z}
                for (i in 0 until 26) {
                    setTransition(69, i+97, 28)
                }
                //line{0..9}
                for (i in 0 until 10) {
                    setTransition(69, i+48, 29)
                }

        //marker______________________________________
            setTransition(1, 'm', 70)
                //m{A..Z}
                for (i in 0 until 26) {
                    setTransition(70, i+65, 28)
                }
                //m{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(70, i+97, 28)
                }
                //m{0..9}
                for (i in 0 until 10) {
                    setTransition(70, i+48, 29)
                }

            setTransition(70, 'a', 71)
                //ma{A..Z}
                for (i in 0 until 26) {
                    setTransition(71, i+65, 28)
                }
                //ma{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(71, i+97, 28)
                }
                //ma{0..9}
                for (i in 0 until 10) {
                    setTransition(71, i+48, 29)
                }

            setTransition(71, 'r', 72)
                //mar{A..Z}
                for (i in 0 until 26) {
                    setTransition(72, i+65, 28)
                }
                //mar{a..z} / k
                for (i in 0 until 26) {
                    if (i+97 == 107) continue // skip k
                    setTransition(72, i+97, 28)
                }
                //mar{0..9}
                for (i in 0 until 10) {
                    setTransition(72, i+48, 29)
                }

            setTransition(72, 'k', 73)
                //mark{A..Z}
                for (i in 0 until 26) {
                    setTransition(73, i+65, 28)
                }
                //mark{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(73, i+97, 28)
                }
                //mark{0..9}
                for (i in 0 until 10) {
                    setTransition(73, i+48, 29)
                }

            setTransition(73, 'e', 74)
                //marke{A..Z}
                for (i in 0 until 26) {
                    setTransition(74, i+65, 28)
                }
                //marke{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(74, i+97, 28)
                }
                //marke{0..9}
                for (i in 0 until 10) {
                    setTransition(74, i+48, 29)
                }

            setTransition(74, 'r', 75)
                //marker{A..Z}
                for (i in 0 until 26) {
                    setTransition(75, i+65, 28)
                }
                //marker{a..z}
                for (i in 0 until 26) {
                    setTransition(75, i+97, 28)
                }
                //marker{0..9}
                for (i in 0 until 10) {
                    setTransition(75, i+48, 29)
                }

        //point______________________________________
            setTransition(1, 'p', 76)
                //p{A..Z}
                for (i in 0 until 26) {
                    setTransition(76, i+65, 28)
                }
                //p{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(76, i+97, 28)
                }
                //p{0..9}
                for (i in 0 until 10) {
                    setTransition(76, i+48, 29)
                }

            setTransition(76, 'o', 77)
                //po{A..Z}
                for (i in 0 until 26) {
                    setTransition(77, i+65, 28)
                }
                //po{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(77, i+97, 28)
                }
                //po{0..9}
                for (i in 0 until 10) {
                    setTransition(77, i+48, 29)
                }

            setTransition(77, 'i', 78)
                //poi{A..Z}
                for (i in 0 until 26) {
                    setTransition(78, i+65, 28)
                }
                //poi{a..z} / n
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(78, i+97, 28)
                }
                //poi{0..9}
                for (i in 0 until 10) {
                    setTransition(78, i+48, 29)
                }

            setTransition(78, 'n', 79)
                //poin{A..Z}
                for (i in 0 until 26) {
                    setTransition(79, i+65, 28)
                }
                //poin{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(79, i+97, 28)
                }
                //poin{0..9}
                for (i in 0 until 10) {
                    setTransition(79, i+48, 29)
                }

            setTransition(79, 't', 80)
                //point{A..Z}
                for (i in 0 until 26) {
                    setTransition(80, i+65, 28)
                }
                //point{a..z}
                for (i in 0 until 26) {
                    setTransition(80, i+97, 28)
                }
                //point{0..9}
                for (i in 0 until 10) {
                    setTransition(80, i+48, 29)
                }

        //routes______________________________________
            setTransition(1, 'r', 81)
                //r{A..Z}
                for (i in 0 until 26) {
                    setTransition(81, i+65, 28)
                }
                //r{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(81, i+97, 28)
                }
                //r{0..9}
                for (i in 0 until 10) {
                    setTransition(81, i+48, 29)
                }

            setTransition(81, 'o', 82)
                //ro{A..Z}
                for (i in 0 until 26) {
                    setTransition(82, i+65, 28)
                }
                //ro{a..z} / u
                for (i in 0 until 26) {
                    if (i+97 == 117) continue // skip u
                    setTransition(82, i+97, 28)
                }
                //ro{0..9}
                for (i in 0 until 10) {
                    setTransition(82, i+48, 29)
                }

            setTransition(82, 'u', 83)
                //rou{A..Z}
                for (i in 0 until 26) {
                    setTransition(83, i+65, 28)
                }
                //rou{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(83, i+97, 28)
                }
                //rou{0..9}
                for (i in 0 until 10) {
                    setTransition(83, i+48, 29)
                }

            setTransition(83, 't', 84)
                //rout{A..Z}
                for (i in 0 until 26) {
                    setTransition(84, i+65, 28)
                }
                //rout{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(84, i+97, 28)
                }
                //rout{0..9}
                for (i in 0 until 10) {
                    setTransition(84, i+48, 29)
                }

            setTransition(84, 'e', 85)
                //route{A..Z}
                for (i in 0 until 26) {
                    setTransition(85, i+65, 28)
                }
                //route{a..z}
                for (i in 0 until 26) {
                    setTransition(85, i+97, 28)
                }
                //route{0..9}
                for (i in 0 until 10) {
                    setTransition(85, i+48, 29)
                }

            setTransition(85, 's', 86)
                //routes{A..Z}
                for (i in 0 until 26) {
                    setTransition(86, i+65, 28)
                }
                //routes{a..z}
                for (i in 0 until 26) {
                    setTransition(86, i+97, 28)
                }
                //routes{0..9}
                for (i in 0 until 10) {
                    setTransition(86, i+48, 29)
                }

        //road______________________________________
            setTransition(1, 'r', 87)
                //r{A..Z}
                for (i in 0 until 26) {
                    setTransition(87, i+65, 28)
                }
                //r{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(87, i+97, 28)
                }
                //r{0..9}
                for (i in 0 until 10) {
                    setTransition(87, i+48, 29)
                }

            setTransition(87, 'o', 88)
                //ro{A..Z}
                for (i in 0 until 26) {
                    setTransition(88, i+65, 28)
                }
                //ro{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(88, i+97, 28)
                }
                //ro{0..9}
                for (i in 0 until 10) {
                    setTransition(88, i+48, 29)
                }

            setTransition(88, 'a', 89)
                //roa{A..Z}
                for (i in 0 until 26) {
                    setTransition(89, i+65, 28)
                }
                //roa{a..z} / d
                for (i in 0 until 26) {
                    if (i+97 == 100) continue // skip d
                    setTransition(89, i+97, 28)
                }
                //roa{0..9}
                for (i in 0 until 10) {
                    setTransition(89, i+48, 29)
                }

            setTransition(89, 'd', 90)
                //road{A..Z}
                for (i in 0 until 26) {
                    setTransition(90, i+65, 28)
                }
                //road{a..z}
                for (i in 0 until 26) {
                    setTransition(90, i+97, 28)
                }
                //road{0..9}
                for (i in 0 until 10) {
                    setTransition(90, i+48, 29)
                }

        //bend______________________________________
            setTransition(1, 'b', 91)
                //b{A..Z}
                for (i in 0 until 26) {
                    setTransition(91, i+65, 28)
                }
                //b{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(91, i+97, 28)
                }
                //b{0..9}
                for (i in 0 until 10) {
                    setTransition(91, i+48, 29)
                }

            setTransition(91, 'e', 92)
                //be{A..Z}
                for (i in 0 until 26) {
                    setTransition(92, i+65, 28)
                }
                //be{a..z} / n
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(92, i+97, 28)
                }
                //be{0..9}
                for (i in 0 until 10) {
                    setTransition(92, i+48, 29)
                }

            setTransition(92, 'n', 93)
                //ben{A..Z}
                for (i in 0 until 26) {
                    setTransition(93, i+65, 28)
                }
                //ben{a..z} / d
                for (i in 0 until 26) {
                    if (i+97 == 100) continue // skip d
                    setTransition(93, i+97, 28)
                }
                //ben{0..9}
                for (i in 0 until 10) {
                    setTransition(93, i+48, 29)
                }

            setTransition(93, 'd', 94)
                //bend{A..Z}
                for (i in 0 until 26) {
                    setTransition(94, i+65, 28)
                }
                //bend{a..z}
                for (i in 0 until 26) {
                    setTransition(94, i+97, 28)
                }
                //bend{0..9}
                for (i in 0 until 10) {
                    setTransition(94, i+48, 29)
                }

        //highlight______________________________________
            setTransition(1, 'h', 95)
                //h{A..Z}
                for (i in 0 until 26) {
                    setTransition(95, i+65, 28)
                }
                //h{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(95, i+97, 28)
                }
                //h{0..9}
                for (i in 0 until 10) {
                    setTransition(95, i+48, 29)
                }

            setTransition(95, 'i', 96)
                //hi{A..Z}
                for (i in 0 until 26) {
                    setTransition(96, i+65, 28)
                }
                //hi{a..z} / g
                for (i in 0 until 26) {
                    if (i+97 == 103) continue // skip g
                    setTransition(96, i+97, 28)
                }
                //hi{0..9}
                for (i in 0 until 10) {
                    setTransition(96, i+48, 29)
                }

            setTransition(96, 'g', 97)
                //hig{A..Z}
                for (i in 0 until 26) {
                    setTransition(97, i+65, 28)
                }
                //hig{a..z} / h
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(97, i+97, 28)
                }
                //hig{0..9}
                for (i in 0 until 10) {
                    setTransition(97, i+48, 29)
                }

            setTransition(97, 'h', 98)
                //high{A..Z}
                for (i in 0 until 26) {
                    setTransition(98, i+65, 28)
                }
                //high{a..z} / l
                for (i in 0 until 26) {
                    if (i+97 == 108) continue // skip l
                    setTransition(98, i+97, 28)
                }
                //high{0..9}
                for (i in 0 until 10) {
                    setTransition(98, i+48, 29)
                }

            setTransition(98, 'l', 99)
                //highl{A..Z}
                for (i in 0 until 26) {
                    setTransition(99, i+65, 28)
                }
                //highl{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(99, i+97, 28)
                }
                //highl{0..9}
                for (i in 0 until 10) {
                    setTransition(99, i+48, 29)
                }

            setTransition(99, 'i', 100)
                //highli{A..Z}
                for (i in 0 until 26) {
                    setTransition(100, i+65, 28)
                }
                //highli{a..z} / g
                for (i in 0 until 26) {
                    if (i+97 == 103) continue // skip g
                    setTransition(100, i+97, 28)
                }
                //highli{0..9}
                for (i in 0 until 10) {
                    setTransition(100, i+48, 29)
                }

            setTransition(100, 'g', 101)
                //highlig{A..Z}
                for (i in 0 until 26) {
                    setTransition(101, i+65, 28)
                }
                //highlig{a..z} / h
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(101, i+97, 28)
                }
                //highlig{0..9}
                for (i in 0 until 10) {
                    setTransition(101, i+48, 29)
                }

            setTransition(101, 'h', 102)
                //highligh{A..Z}
                for (i in 0 until 26) {
                    setTransition(102, i+65, 28)
                }
                //highligh{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(102, i+97, 28)
                }
                //highligh{0..9}
                for (i in 0 until 10) {
                    setTransition(102, i+48, 29)
                }

            setTransition(102, 't', 103)
                //highlight{A..Z}
                for (i in 0 until 26) {
                    setTransition(103, i+65, 28)
                }
                //highlight{a..z}
                for (i in 0 until 26) {
                    setTransition(103, i+97, 28)
                }
                //highlight{0..9}
                for (i in 0 until 10) {
                    setTransition(103, i+48, 29)
                }

        //foreach______________________________________
            setTransition(1, 'f', 104)
                //f{A..Z}
                for (i in 0 until 26) {
                    setTransition(104, i+65, 28)
                }
                //f{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(104, i+97, 28)
                }
                //f{0..9}
                for (i in 0 until 10) {
                    setTransition(104, i+48, 29)
                }

            setTransition(104, 'o', 105)
                //fo{A..Z}
                for (i in 0 until 26) {
                    setTransition(105, i+65, 28)
                }
                //fo{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(105, i+97, 28)
                }
                //fo{0..9}
                for (i in 0 until 10) {
                    setTransition(105, i+48, 29)
                }

            setTransition(105, 'r', 106)
                //for{A..Z}
                for (i in 0 until 26) {
                    setTransition(106, i+65, 28)
                }
                //for{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(106, i+97, 28)
                }
                //for{0..9}
                for (i in 0 until 10) {
                    setTransition(106, i+48, 29)
                }

            setTransition(106, 'e', 107)
                //fore{A..Z}
                for (i in 0 until 26) {
                    setTransition(107, i+65, 28)
                }
                //fore{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(107, i+97, 28)
                }
                //fore{0..9}
                for (i in 0 until 10) {
                    setTransition(107, i+48, 29)
                }

            setTransition(107, 'a', 108)
                //forea{A..Z}
                for (i in 0 until 26) {
                    setTransition(108, i+65, 28)
                }
                //forea{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 99) continue // skip c
                    setTransition(108, i+97, 28)
                }
                //forea{0..9}
                for (i in 0 until 10) {
                    setTransition(108, i+48, 29)
                }

            setTransition(108, 'c', 109)
                //foreac{A..Z}
                for (i in 0 until 26) {
                    setTransition(109, i+65, 28)
                }
                //foreac{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(109, i+97, 28)
                }
                //foreac{0..9}
                for (i in 0 until 10) {
                    setTransition(109, i+48, 29)
                }

            setTransition(109, 'h', 110)
                //foreach{A..Z}
                for (i in 0 until 26) {
                    setTransition(110, i+65, 28)
                }
                //foreach{a..z}
                for (i in 0 until 26) {
                    setTransition(110, i+97, 28)
                }
                //foreach{0..9}
                for (i in 0 until 10) {
                    setTransition(110, i+48, 29)
                }

        //print________________________________________
            setTransition(1, 'p', 111)
                //p{A..Z}
                for (i in 0 until 26) {
                    setTransition(111, i+65, 28)
                }
                //p{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(111, i+97, 28)
                }
                //p{0..9}
                for (i in 0 until 10) {
                    setTransition(111, i+48, 29)
                }

            setTransition(111, 'r', 112)
                //pr{A..Z}
                for (i in 0 until 26) {
                    setTransition(112, i+65, 28)
                }
                //pr{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(112, i+97, 28)
                }
                //pr{0..9}
                for (i in 0 until 10) {
                    setTransition(112, i+48, 29)
                }

            setTransition(112, 'i', 113)
                //pri{A..Z}
                for (i in 0 until 26) {
                    setTransition(113, i+65, 28)
                }
                //pri{a..z} / n
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(113, i+97, 28)
                }
                //pri{0..9}
                for (i in 0 until 10) {
                    setTransition(113, i+48, 29)
                }

            setTransition(113, 'n', 114)
                //prin{A..Z}
                for (i in 0 until 26) {
                    setTransition(114, i+65, 28)
                }
                //prin{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(114, i+97, 28)
                }
                //prin{0..9}
                for (i in 0 until 10) {
                    setTransition(114, i+48, 29)
                }

            setTransition(114, 't', 115)
                //print{A..Z}
                for (i in 0 until 26) {
                    setTransition(115, i+65, 28)
                }
                //print{a..z}
                for (i in 0 until 26) {
                    setTransition(115, i+97, 28)
                }
                //print{0..9}
                for (i in 0 until 10) {
                    setTransition(115, i+48, 29)
                }

        //in___________________________________________
            setTransition(1, 'i', 116)
                //i{A..Z}
                for (i in 0 until 26) {
                    setTransition(116, i+65, 28)
                }
                //i{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(116, i+97, 28)
                }
                //i{0..9}
                for (i in 0 until 10) {
                    setTransition(116, i+48, 29)
                }

            setTransition(116, 'n', 117)
                //in{A..Z}
                for (i in 0 until 26) {
                    setTransition(117, i+65, 28)
                }
                //in{a..z} / o
                for (i in 0 until 26) {
                    setTransition(117, i+97, 28)
                }
                //in{0..9}
                for (i in 0 until 10) {
                    setTransition(117, i+48, 29)
                }

        //^"[A-Za-z0-9\s\S]*"$_______________________
            setTransition(1, )

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




        setSymbol(2, Symbol.REAL)
        setSymbol(4, Symbol.REAL)
        setSymbol(5, Symbol.LTHAN)
        setSymbol(6, Symbol.MTHAN)
        setSymbol(7, Symbol.BLOCK_START)
        setSymbol(8, Symbol.BLOCK_END)
        setSymbol(9, Symbol.COLON)
        setSymbol(10, Symbol.LSQ_BRACKET)
        setSymbol(11, Symbol.COMMA)
        setSymbol(12, Symbol.RSQ_BRACKET)
        setSymbol(13, Symbol.LBRACKET)
        setSymbol(14, Symbol.RBRACKET)
        setSymbol(15, Symbol.SEMICOL)
        setSymbol(16, Symbol.HASH)
        setSymbol(17, Symbol.PLUS)
        setSymbol(18, Symbol.MINUS)
        setSymbol(19, Symbol.TIMES)
        setSymbol(20, Symbol.DIVIDES)
        setSymbol(21, Symbol.INTIGERDIVIDES)
        setSymbol(22, Symbol.POW)
        setSymbol(23, Symbol.SKIP)
        setSymbol(24, Symbol.SKIP)
        setSymbol(25, Symbol.SKIP)
        setSymbol(26, Symbol.SKIP)
        setSymbol(27, Symbol.EOF)

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
