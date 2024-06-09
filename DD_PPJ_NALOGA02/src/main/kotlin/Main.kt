import java.io.InputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.*

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
    INTEGERDIVIDES,
    POW,
    PRINT,
    IN,
    DOT,
    RESTAURANT,
    RADIUS
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
    override val states = (1 .. 126).toSet()
    override val alphabet = 0 .. 255
    override val startState = 1
    override val finalStates = setOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
        31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
        51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
        61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
        71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
        81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
        91, 92, 93, 94, 95, 96, 97, 98, 99, 100,
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
        121, 122, 123, 124, 125, 126
    )
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
        setTransition(1, '-', 18)
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
        setTransition(1, '_', 28)

        //marker______________________________________
            setTransition(1, 'm', 30)
                //m{A..Z}
                for (i in 0 until 26) {
                    setTransition(30, i+65, 28)
                }
                //m{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(30, i+97, 28)
                }
                //m{0..9}
                for (i in 0 until 10) {
                    setTransition(30, i+48, 29)
                }

            setTransition(30, 'a', 31)
                //ma{A..Z}
                for (i in 0 until 26) {
                    setTransition(31, i+65, 28)
                }
                //ma{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(31, i+97, 28)
                }
                //ma{0..9}
                for (i in 0 until 10) {
                    setTransition(31, i+48, 29)
                }

            setTransition(31, 'r', 32)
                //mar{A..Z}
                for (i in 0 until 26) {
                    setTransition(32, i+65, 28)
                }
                //mar{a..z} / k
                for (i in 0 until 26) {
                    if (i+97 == 107) continue // skip k
                    setTransition(32, i+97, 28)
                }
                //mar{0..9}
                for (i in 0 until 10) {
                    setTransition(32, i+48, 29)
                }

            setTransition(32, 'k', 33)
                //mark{A..Z}
                for (i in 0 until 26) {
                    setTransition(33, i+65, 28)
                }
                //mark{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(33, i+97, 28)
                }
                //mark{0..9}
                for (i in 0 until 10) {
                    setTransition(33, i+48, 29)
                }

            setTransition(33, 'e', 34)
                //marke{A..Z}
                for (i in 0 until 26) {
                    setTransition(34, i+65, 28)
                }
                //marke{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(34, i+97, 28)
                }
                //marke{0..9}
                for (i in 0 until 10) {
                    setTransition(34, i+48, 29)
                }

            setTransition(34, 'r', 35)
                //marker{A..Z}
                for (i in 0 until 26) {
                    setTransition(35, i+65, 28)
                }
                //marker{a..z}
                for (i in 0 until 26) {
                    setTransition(35, i+97, 28)
                }
                //marker{0..9}
                for (i in 0 until 10) {
                    setTransition(35, i+48, 29)
                }

        //bend______________________________________
            setTransition(1, 'b', 36)
                //b{A..Z}
                for (i in 0 until 26) {
                    setTransition(36, i+65, 28)
                }
                //b{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(36, i+97, 28)
                }
                //b{0..9}
                for (i in 0 until 10) {
                    setTransition(36, i+48, 29)
                }

            setTransition(36, 'e', 37)
                //be{A..Z}
                for (i in 0 until 26) {
                    setTransition(37, i+65, 28)
                }
                //be{a..z} / n
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(37, i+97, 28)
                }
                //be{0..9}
                for (i in 0 until 10) {
                    setTransition(37, i+48, 29)
                }

            setTransition(37, 'n', 38)
                //ben{A..Z}
                for (i in 0 until 26) {
                    setTransition(38, i+65, 28)
                }
                //ben{a..z} / d
                for (i in 0 until 26) {
                    if (i+97 == 100) continue // skip d
                    setTransition(38, i+97, 28)
                }
                //ben{0..9}
                for (i in 0 until 10) {
                    setTransition(38, i+48, 29)
                }

            setTransition(38, 'd', 39)
                //bend{A..Z}
                for (i in 0 until 26) {
                    setTransition(39, i+65, 28)
                }
                //bend{a..z}
                for (i in 0 until 26) {
                    setTransition(39, i+97, 28)
                }
                //bend{0..9}
                for (i in 0 until 10) {
                    setTransition(39, i+48, 29)
                }

        //highlight______________________________________
            setTransition(1, 'h', 40)
                //h{A..Z}
                for (i in 0 until 26) {
                    setTransition(40, i+65, 28)
                }
                //h{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(40, i+97, 28)
                }
                //h{0..9}
                for (i in 0 until 10) {
                    setTransition(40, i+48, 29)
                }

            setTransition(40, 'i', 41)
                //hi{A..Z}
                for (i in 0 until 26) {
                    setTransition(41, i+65, 28)
                }
                //hi{a..z} / g
                for (i in 0 until 26) {
                    if (i+97 == 103) continue // skip g
                    setTransition(41, i+97, 28)
                }
                //hi{0..9}
                for (i in 0 until 10) {
                    setTransition(41, i+48, 29)
                }

            setTransition(41, 'g', 42)
                //hig{A..Z}
                for (i in 0 until 26) {
                    setTransition(42, i+65, 28)
                }
                //hig{a..z} / h
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(42, i+97, 28)
                }
                //hig{0..9}
                for (i in 0 until 10) {
                    setTransition(42, i+48, 29)
                }

            setTransition(42, 'h', 43)
                //high{A..Z}
                for (i in 0 until 26) {
                    setTransition(43, i+65, 28)
                }
                //high{a..z} / l
                for (i in 0 until 26) {
                    if (i+97 == 108) continue // skip l
                    setTransition(43, i+97, 28)
                }
                //high{0..9}
                for (i in 0 until 10) {
                    setTransition(43, i+48, 29)
                }

            setTransition(43, 'l', 44)
                //highl{A..Z}
                for (i in 0 until 26) {
                    setTransition(44, i+65, 28)
                }
                //highl{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(44, i+97, 28)
                }
                //highl{0..9}
                for (i in 0 until 10) {
                    setTransition(44, i+48, 29)
                }

            setTransition(44, 'i', 45)
                //highli{A..Z}
                for (i in 0 until 26) {
                    setTransition(45, i+65, 28)
                }
                //highli{a..z} / g
                for (i in 0 until 26) {
                    if (i+97 == 103) continue // skip g
                    setTransition(45, i+97, 28)
                }
                //highli{0..9}
                for (i in 0 until 10) {
                    setTransition(45, i+48, 29)
                }

            setTransition(45, 'g', 46)
                //highlig{A..Z}
                for (i in 0 until 26) {
                    setTransition(46, i+65, 28)
                }
                //highlig{a..z} / h
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(46, i+97, 28)
                }
                //highlig{0..9}
                for (i in 0 until 10) {
                    setTransition(46, i+48, 29)
                }

            setTransition(46, 'h', 47)
                //highligh{A..Z}
                for (i in 0 until 26) {
                    setTransition(47, i+65, 28)
                }
                //highligh{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(47, i+97, 28)
                }
                //highligh{0..9}
                for (i in 0 until 10) {
                    setTransition(47, i+48, 29)
                }

            setTransition(47, 't', 48)
                //highlight{A..Z}
                for (i in 0 until 26) {
                    setTransition(48, i+65, 28)
                }
                //highlight{a..z}
                for (i in 0 until 26) {
                    setTransition(48, i+97, 28)
                }
                //highlight{0..9}
                for (i in 0 until 10) {
                    setTransition(48, i+48, 29)
                }

        //foreach______________________________________
            setTransition(1, 'f', 49)
                //f{A..Z}
                for (i in 0 until 26) {
                    setTransition(49, i+65, 28)
                }
                //f{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(49, i+97, 28)
                }
                //f{0..9}
                for (i in 0 until 10) {
                    setTransition(49, i+48, 29)
                }

            setTransition(49, 'o', 50)
                //fo{A..Z}
                for (i in 0 until 26) {
                    setTransition(50, i+65, 28)
                }
                //fo{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(50, i+97, 28)
                }
                //fo{0..9}
                for (i in 0 until 10) {
                    setTransition(50, i+48, 29)
                }

            setTransition(50, 'r', 51)
                //for{A..Z}
                for (i in 0 until 26) {
                    setTransition(51, i+65, 28)
                }
                //for{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(51, i+97, 28)
                }
                //for{0..9}
                for (i in 0 until 10) {
                    setTransition(51, i+48, 29)
                }

            setTransition(51, 'e', 52)
                //fore{A..Z}
                for (i in 0 until 26) {
                    setTransition(52, i+65, 28)
                }
                //fore{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(52, i+97, 28)
                }
                //fore{0..9}
                for (i in 0 until 10) {
                    setTransition(52, i+48, 29)
                }

            setTransition(52, 'a', 53)
                //forea{A..Z}
                for (i in 0 until 26) {
                    setTransition(53, i+65, 28)
                }
                //forea{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 99) continue // skip c
                    setTransition(53, i+97, 28)
                }
                //forea{0..9}
                for (i in 0 until 10) {
                    setTransition(53, i+48, 29)
                }

            setTransition(53, 'c', 54)
                //foreac{A..Z}
                for (i in 0 until 26) {
                    setTransition(54, i+65, 28)
                }
                //foreac{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    setTransition(54, i+97, 28)
                }
                //foreac{0..9}
                for (i in 0 until 10) {
                    setTransition(54, i+48, 29)
                }

            setTransition(54, 'h', 55)
                //foreach{A..Z}
                for (i in 0 until 26) {
                    setTransition(55, i+65, 28)
                }
                //foreach{a..z}
                for (i in 0 until 26) {
                    setTransition(55, i+97, 28)
                }
                //foreach{0..9}
                for (i in 0 until 10) {
                    setTransition(55, i+48, 29)
                }

        //in___________________________________________
            setTransition(1, 'i', 56)
                //i{A..Z}
                for (i in 0 until 26) {
                    setTransition(56, i+65, 28)
                }
                //i{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(56, i+97, 28)
                }
                //i{0..9}
                for (i in 0 until 10) {
                    setTransition(56, i+48, 29)
                }

            setTransition(56, 'n', 57)
                //in{A..Z}
                for (i in 0 until 26) {
                    setTransition(57, i+65, 28)
                }
                //in{a..z} / o
                for (i in 0 until 26) {
                    setTransition(57, i+97, 28)
                }
                //in{0..9}
                for (i in 0 until 10) {
                    setTransition(57, i+48, 29)
                }

        //^"[A-Za-z0-9\s\S]*"$_______________________
            setTransition(1, '"', 58)
                //""
                setTransition(58, '"', 74)
                //"{A..Z}
                for (i in 0 until 26) {
                    setTransition(58, i+65, 59)
                }
                //"{a..z}
                for (i in 0 until 26) {
                    setTransition(58, i+97, 59)
                }
                //"{0..9}
                for (i in 0 until 10) {
                    setTransition(58, i+48, 59)
                }
                //"{\s}
                for (i in 0 until 15) {
                    setTransition(58, i+32, 59)
                }
                //"{\n\t}
                setTransition(58, '\n', 59)
                setTransition(58, '\t', 59)

            //"{A..Z}*
            for (i in 0 until 26) {
                setTransition(59, i+65, 59)
            }
            //"{a..z}*
            for (i in 0 until 26) {
                setTransition(59, i+97, 59)
            }
            //"{0..9}*
            for (i in 0 until 10) {
                setTransition(59, i+48, 59)
            }
            //"{\s}*
            for (i in 0 until 15) {
                setTransition(59, i+32, 59)
            }
            //"{\n\t}*
            setTransition(59, '\n', 59)
            setTransition(59, '\t', 59)

            //"[A-Za-z0-9\s\S]*"
            setTransition(59, '"', 74)

        //point______________________________________
            setTransition(1, 'p', 60)
                //p{A..Z}
                for (i in 0 until 26) {
                    setTransition(60, i+65, 28)
                }
                //p{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    if (i+97 == 114) continue // skip r
                    setTransition(60, i+97, 28)
                }
                //p{0..9}
                for (i in 0 until 10) {
                    setTransition(60, i+48, 29)
                }

                //point
                    setTransition(60, 'o', 61)
                    //po{A..Z}
                    for (i in 0 until 26) {
                        setTransition(61, i+65, 28)
                    }
                    //po{a..z} / i
                    for (i in 0 until 26) {
                        if (i+97 == 105) continue // skip i
                        setTransition(61, i+97, 28)
                    }
                    //po{0..9}
                    for (i in 0 until 10) {
                        setTransition(61, i+48, 29)
                    }

                    setTransition(61, 'i', 62)
                    //poi{A..Z}
                    for (i in 0 until 26) {
                        setTransition(62, i+65, 28)
                    }
                    //poi{a..z} / n
                    for (i in 0 until 26) {
                        if (i+97 == 110) continue // skip n
                        setTransition(62, i+97, 28)
                    }
                    //poi{0..9}
                    for (i in 0 until 10) {
                        setTransition(62, i+48, 29)
                    }

                    setTransition(62, 'n', 63)
                    //poin{A..Z}
                    for (i in 0 until 26) {
                        setTransition(63, i+65, 28)
                    }
                    //poin{a..z} / t
                    for (i in 0 until 26) {
                        if (i+97 == 116) continue // skip t
                        setTransition(63, i+97, 28)
                    }
                    //poin{0..9}
                    for (i in 0 until 10) {
                        setTransition(63, i+48, 29)
                    }

                    setTransition(63, 't', 64)
                    //point{A..Z}
                    for (i in 0 until 26) {
                        setTransition(64, i+65, 28)
                    }
                    //point{a..z}
                    for (i in 0 until 26) {
                        setTransition(64, i+97, 28)
                    }
                    //point{0..9}
                    for (i in 0 until 10) {
                        setTransition(64, i+48, 29)
                    }

                //print
                    setTransition(60, 'r', 65)
                    //pr{A..Z}
                    for (i in 0 until 26) {
                        setTransition(65, i+65, 28)
                    }
                    //pr{a..z} / i
                    for (i in 0 until 26) {
                        if (i+97 == 105) continue // skip i
                        setTransition(65, i+97, 28)
                    }
                    //pr{0..9}
                    for (i in 0 until 10) {
                        setTransition(65, i+48, 29)
                    }

                    setTransition(65, 'i', 66)
                    //pri{A..Z}
                    for (i in 0 until 26) {
                        setTransition(66, i+65, 28)
                    }
                    //pri{a..z} / n
                    for (i in 0 until 26) {
                        if (i+97 == 110) continue // skip n
                        setTransition(66, i+97, 28)
                    }
                    //pri{0..9}
                    for (i in 0 until 10) {
                        setTransition(66, i+48, 29)
                    }

                    setTransition(66, 'n', 67)
                    //prin{A..Z}
                    for (i in 0 until 26) {
                        setTransition(67, i+65, 28)
                    }
                    //prin{a..z} / t
                    for (i in 0 until 26) {
                        if (i+97 == 116) continue // skip t
                        setTransition(67, i+97, 28)
                    }
                    //prin{0..9}
                    for (i in 0 until 10) {
                        setTransition(67, i+48, 29)
                    }

                    setTransition(67, 't', 68)
                    //print{A..Z}
                    for (i in 0 until 26) {
                        setTransition(68, i+65, 28)
                    }
                    //print{a..z}
                    for (i in 0 until 26) {
                        setTransition(68, i+97, 28)
                    }
                    //print{0..9}
                    for (i in 0 until 10) {
                        setTransition(68, i+48, 29)
                    }

        //coord______________________________________
            setTransition(1, 'c', 69)
                //c{A..Z}
                for (i in 0 until 26) {
                    setTransition(69, i+65, 28)
                }
                //c{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    if (i+97 == 105) continue // skip i
                    setTransition(69, i+97, 28)
                }
                //c{0..9}
                for (i in 0 until 10) {
                    setTransition(69, i+48, 29)
                }

                //co
                setTransition(69, 'o', 70)
                    //co{A..Z}
                    for (i in 0 until 26) {
                        setTransition(70, i+65, 28)
                    }
                    //co{a..z} / o
                    for (i in 0 until 26) {
                        if (i+97 == 111) continue // skip o
                        setTransition(70, i+97, 28)
                    }
                    //co{0..9}
                    for (i in 0 until 10) {
                        setTransition(70, i+48, 29)
                    }

                    //coord
                        setTransition(70, 'o', 71)
                        //coo{A..Z}
                        for (i in 0 until 26) {
                            setTransition(71, i+65, 28)
                        }
                        //coo{a..z} / r
                        for (i in 0 until 26) {
                            if (i+97 == 114) continue // skip r
                            setTransition(71, i+97, 28)
                        }
                        //coo{0..9}
                        for (i in 0 until 10) {
                            setTransition(71, i+48, 29)
                        }

                        setTransition(71, 'r', 72)
                        //coor{A..Z}
                        for (i in 0 until 26) {
                            setTransition(72, i+65, 28)
                        }
                        //coor{a..z} / d
                        for (i in 0 until 26) {
                            if (i+97 == 100) continue // skip d
                            setTransition(72, i+97, 28)
                        }
                        //coor{0..9}
                        for (i in 0 until 10) {
                            setTransition(72, i+48, 29)
                        }

                        setTransition(72, 'd', 73)
                        //coord{A..Z}
                        for (i in 0 until 26) {
                            setTransition(73, i+65, 28)
                        }
                        //coord{a..z}
                        for (i in 0 until 26) {
                            setTransition(73, i+97, 28)
                        }
                        //coord{0..9}
                        for (i in 0 until 10) {
                            setTransition(73, i+48, 29)
                        }

                        ////////////////////////////////////////////////////////////
                        ////////////////////////////////////////////////////////////
                        //////////////////////////74 je ",/////////////////////////
                        //////////////////////////ker sn se/////////////////////////
                        //////////////////////////zmoto/////////////////////////////
                        ////////////////////////////////////////////////////////////
                        ////////////////////////////////////////////////////////////

                    //const
                        setTransition(70, 'n', 75)
                        //con{A..Z}
                        for (i in 0 until 26) {
                            setTransition(75, i+65, 28)
                        }
                        //con{a..z} / s
                        for (i in 0 until 26) {
                            if (i+97 == 115) continue // skip s
                            setTransition(75, i+97, 28)
                        }
                        //con{0..9}
                        for (i in 0 until 10) {
                            setTransition(75, i+48, 29)
                        }

                        setTransition(75, 's', 76)
                        //cons{A..Z}
                        for (i in 0 until 26) {
                            setTransition(76, i+65, 28)
                        }
                        //cons{a..z} / t
                        for (i in 0 until 26) {
                            if (i+97 == 116) continue // skip t
                            setTransition(76, i+97, 28)
                        }
                        //const{0..9}
                        for (i in 0 until 10) {
                            setTransition(76, i+48, 29)
                        }

                        setTransition(76, 't', 77)
                        //const{A..Z}
                        for (i in 0 until 26) {
                            setTransition(77, i+65, 28)
                        }
                        //const{a..z}
                        for (i in 0 until 26) {
                            setTransition(77, i+97, 28)
                        }
                        //const{0..9}
                        for (i in 0 until 10) {
                            setTransition(77, i+48, 29)
                        }

                //city
                    setTransition(69, 'i', 78)
                    //ci{A..Z}
                    for (i in 0 until 26) {
                        setTransition(78, i+65, 28)
                    }
                    //ci{a..z} / t
                    for (i in 0 until 26) {
                        if (i+97 == 116) continue // skip t
                        setTransition(78, i+97, 28)
                    }
                    //ci{0..9}
                    for (i in 0 until 10) {
                        setTransition(78, i+48, 29)
                    }

                    setTransition(78, 't', 79)
                    //cit{A..Z}
                    for (i in 0 until 26) {
                        setTransition(79, i+65, 28)
                    }
                    //cit{a..z} / y
                    for (i in 0 until 26) {
                        if (i+97 == 121) continue // skip y
                        setTransition(79, i+97, 28)
                    }
                    //cit{0..9}
                    for (i in 0 until 10) {
                        setTransition(79, i+48, 29)
                    }

                    setTransition(79, 'y', 80)
                    //city{A..Z}
                    for (i in 0 until 26) {
                        setTransition(80, i+65, 28)
                    }
                    //city{a..z}
                    for (i in 0 until 26) {
                        setTransition(80, i+97, 28)
                    }
                    //city{0..9}
                    for (i in 0 until 10) {
                        setTransition(80, i+48, 29)
                    }

        //name______________________________________
            setTransition(1, 'n', 81)
                //n{A..Z}
                for (i in 0 until 26) {
                    setTransition(81, i+65, 28)
                }
                //n{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    if (i+97 == 117) continue // skip u
                    setTransition(81, i+97, 28)
                }
                //n{0..9}
                for (i in 0 until 10) {
                    setTransition(81, i+48, 29)
                }

                //name
                    setTransition(81, 'a', 82)
                    //na{A..Z}
                    for (i in 0 until 26) {
                        setTransition(82, i+65, 28)
                    }
                    //na{a..z} / m
                    for (i in 0 until 26) {
                        if (i+97 == 109) continue // skip m
                        setTransition(82, i+97, 28)
                    }
                    //na{0..9}
                    for (i in 0 until 10) {
                        setTransition(82, i+48, 29)
                    }

                    setTransition(82, 'm', 83)
                    //nam{A..Z}
                    for (i in 0 until 26) {
                        setTransition(83, i+65, 28)
                    }
                    //nam{a..z} / e
                    for (i in 0 until 26) {
                        if (i+97 == 101) continue // skip e
                        setTransition(83, i+97, 28)
                    }
                    //nam{0..9}
                    for (i in 0 until 10) {
                        setTransition(83, i+48, 29)
                    }

                    setTransition(83, 'e', 84)
                    //name{A..Z}
                    for (i in 0 until 26) {
                        setTransition(84, i+65, 28)
                    }
                    //name{a..z}
                    for (i in 0 until 26) {
                        setTransition(84, i+97, 28)
                    }
                    //name{0..9}
                    for (i in 0 until 10) {
                        setTransition(84, i+48, 29)
                    }

            //num
                setTransition(81, 'u', 119)
                //nu{A..Z}
                for (i in 0 until 26) {
                    setTransition(119, i+65, 28)
                }
                //nu{a..z} / m
                for (i in 0 until 26) {
                    if (i+97 == 109) continue // skip m
                    setTransition(119, i+97, 28)
                }
                //nu{0..9}
                for (i in 0 until 10) {
                    setTransition(119, i+48, 29)
                }

                setTransition(119, 'm', 120)
                //num{A..Z}
                for (i in 0 until 26) {
                    setTransition(120, i+65, 28)
                }
                //num{a..z}
                for (i in 0 until 26) {
                    setTransition(120, i+97, 28)
                }
                //num{0..9}
                for (i in 0 until 10) {
                    setTransition(120, i+48, 29)
                }

        //shape______________________________________
            setTransition(1, 's', 121)
                //s{A..Z}
                for (i in 0 until 26) {
                    setTransition(121, i+65, 28)
                }
                //s{a..z} / h
                for (i in 0 until 26) {
                    if (i+97 == 104) continue // skip h
                    if (i+97 == 116) continue // skip t
                    setTransition(121, i+97, 28)
                }
                //s{0..9}
                for (i in 0 until 10) {
                    setTransition(121, i+48, 29)
                }

            //shape
                setTransition(121, 'h', 85)
                //sh{A..Z}
                for (i in 0 until 26) {
                    setTransition(85, i+65, 28)
                }
                //sh{a..z} / a
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(85, i+97, 28)
                }
                //sh{0..9}
                for (i in 0 until 10) {
                    setTransition(85, i+48, 29)
                }

                setTransition(85, 'a', 86)
                //sha{A..Z}
                for (i in 0 until 26) {
                    setTransition(86, i+65, 28)
                }
                //sha{a..z} / p
                for (i in 0 until 26) {
                    if (i+97 == 112) continue // skip p
                    setTransition(86, i+97, 28)
                }
                //sha{0..9}
                for (i in 0 until 10) {
                    setTransition(86, i+48, 29)
                }

                setTransition(86, 'p', 87)
                //shap{A..Z}
                for (i in 0 until 26) {
                    setTransition(87, i+65, 28)
                }
                //shap{a..z} / e
                for (i in 0 until 26) {
                    if (i+97 == 101) continue // skip e
                    setTransition(87, i+97, 28)
                }
                //shap{0..9}
                for (i in 0 until 10) {
                    setTransition(87, i+48, 29)
                }

                setTransition(87, 'e', 88)
                //shape{A..Z}
                for (i in 0 until 26) {
                    setTransition(88, i+65, 28)
                }
                //shape{a..z}
                for (i in 0 until 26) {
                    setTransition(88, i+97, 28)
                }
                //shape{0..9}
                for (i in 0 until 10) {
                    setTransition(88, i+48, 29)
                }

            //string
                setTransition(121, 't', 89)
                //st{A..Z}
                for (i in 0 until 26) {
                    setTransition(89, i+65, 28)
                }
                //st{a..z} / r
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(89, i+97, 28)
                }
                //st{0..9}
                for (i in 0 until 10) {
                    setTransition(89, i+48, 29)
                }

                setTransition(89, 'r', 90)
                //str{A..Z}
                for (i in 0 until 26) {
                    setTransition(90, i+65, 28)
                }
                //str{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(90, i+97, 28)
                }
                //str{0..9}
                for (i in 0 until 10) {
                    setTransition(90, i+48, 29)
                }

                setTransition(90, 'i', 91)
                //stri{A..Z}
                for (i in 0 until 26) {
                    setTransition(91, i+65, 28)
                }
                //stri{a..z} / n
                for (i in 0 until 26) {
                    if (i + 97 == 110) continue // skip n
                    setTransition(91, i + 97, 28)
                }
                //stri{0..9}
                for (i in 0 until 10) {
                    setTransition(91, i + 48, 29)
                }

                setTransition(91, 'n', 92)
                //strin{A..Z}
                for (i in 0 until 26) {
                    setTransition(92, i+65, 28)
                }
                //strin{a..z} / g
                for (i in 0 until 26) {
                    if (i + 97 == 103) continue // skip g
                    setTransition(92, i + 97, 28)
                }
                //strin{0..9}
                for (i in 0 until 10) {
                    setTransition(92, i + 48, 29)
                }

                setTransition(92, 'g', 93)
                //string{A..Z}
                for (i in 0 until 26) {
                    setTransition(93, i+65, 28)
                }
                //string{a..z}
                for (i in 0 until 26) {
                    setTransition(93, i+97, 28)
                }
                //string{0..9}
                for (i in 0 until 10) {
                    setTransition(93, i+48, 29)
                }

        //list & line_____________________________________
            setTransition(1, 'l', 94)
                //l{A..Z}
                for (i in 0 until 26) {
                    setTransition(94, i+65, 28)
                }
                //l{a..z} / i
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(94, i+97, 28)
                }
                //l{0..9}
                for (i in 0 until 10) {
                    setTransition(94, i+48, 29)
                }

            setTransition(94, 'i', 95)
                //li{A..Z}
                for (i in 0 until 26) {
                    setTransition(95, i+65, 28)
                }
                //li{a..z} / s
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    if (i+97 == 115) continue // skip s
                    setTransition(95, i+97, 28)
                }
                //li{0..9}
                for (i in 0 until 10) {
                    setTransition(95, i+48, 29)
                }

                //list
                setTransition(95, 's', 96)
                    //lis{A..Z}
                    for (i in 0 until 26) {
                        setTransition(96, i+65, 28)
                    }
                    //lis{a..z} / t
                    for (i in 0 until 26) {
                        if (i+97 == 116) continue // skip t
                        setTransition(96, i+97, 28)
                    }
                    //lis{0..9}
                    for (i in 0 until 10) {
                        setTransition(96, i+48, 29)
                    }

                setTransition(96, 't', 97)
                    //list{A..Z}
                    for (i in 0 until 26) {
                        setTransition(97, i+65, 28)
                    }
                    //list{a..z} / t
                    for (i in 0 until 26) {
                        setTransition(97, i+97, 28)
                    }
                    //list{0..9}
                    for (i in 0 until 10) {
                        setTransition(97, i+48, 29)
                    }

                //line
                setTransition(95, 'n', 98)
                    //lin{A..Z}
                    for (i in 0 until 26) {
                        setTransition(98, i+65, 28)
                    }
                    //lin{a..z} / e
                    for (i in 0 until 26) {
                        if (i+97 == 101) continue // skip e
                        setTransition(98, i+97, 28)
                    }
                    //lin{0..9}
                    for (i in 0 until 10) {
                        setTransition(98, i+48, 29)
                    }

                    setTransition(98, 'e', 99)
                    //line{A..Z}
                    for (i in 0 until 26) {
                        setTransition(99, i+65, 28)
                    }
                    //line{a..z}
                    for (i in 0 until 26) {
                        setTransition(99, i+97, 28)
                    }
                    //line{0..9}
                    for (i in 0 until 10) {
                        setTransition(99, i+48, 29)
                    }

        //routes, road, restaurant, radius_____________________________________
            setTransition(1, 'r', 100)
                //r{A..Z}
                for (i in 0 until 26) {
                    setTransition(100, i+65, 28)
                }
                //r{a..z} / o
                for (i in 0 until 26) {
                    if (i+97 == 111) continue // skip o
                    setTransition(100, i+97, 28)
                }
                //r{0..9}
                for (i in 0 until 10) {
                    setTransition(100, i+48, 29)
                }

            //ro
                setTransition(100, 'o', 101)
                //ro{A..Z}
                for (i in 0 until 26) {
                    setTransition(101, i+65, 28)
                }
                //ro{a..z} / u
                for (i in 0 until 26) {
                    if (i+97 == 117) continue // skip u
                    if (i+97 == 97) continue // skip a
                    setTransition(101, i+97, 28)
                }
                //ro{0..9}
                for (i in 0 until 10) {
                    setTransition(101, i+48, 29)
                }

                //routes
                    setTransition(101, 'u', 102)
                    //rou{A..Z}
                    for (i in 0 until 26) {
                        setTransition(102, i+65, 28)
                    }
                    //rou{a..z} / t
                    for (i in 0 until 26) {
                        if (i+97 == 116) continue // skip t
                        setTransition(102, i+97, 28)
                    }
                    //rou{0..9}
                    for (i in 0 until 10) {
                        setTransition(102, i+48, 29)
                    }

                    setTransition(102, 't', 103)
                    //rout{A..Z}
                    for (i in 0 until 26) {
                        setTransition(103, i+65, 28)
                    }
                    //rout{a..z} / e
                    for (i in 0 until 26) {
                        if (i+97 == 101) continue // skip e
                        setTransition(103, i+97, 28)
                    }
                    //rout{0..9}
                    for (i in 0 until 10) {
                        setTransition(103, i+48, 29)
                    }

                    setTransition(103, 'e', 104)
                    //route{A..Z}
                    for (i in 0 until 26) {
                        setTransition(104, i+65, 28)
                    }
                    //route{a..z}
                    for (i in 0 until 26) {
                        setTransition(104, i+97, 28)
                    }
                    //route{0..9}
                    for (i in 0 until 10) {
                        setTransition(104, i+48, 29)
                    }

                    setTransition(104, 's', 105)
                    //routes{A..Z}
                    for (i in 0 until 26) {
                        setTransition(105, i+65, 28)
                    }
                    //routes{a..z}
                    for (i in 0 until 26) {
                        setTransition(105, i+97, 28)
                    }
                    //routes{0..9}
                    for (i in 0 until 10) {
                        setTransition(105, i+48, 29)
                    }

                //road
                    setTransition(101, 'a', 106)
                    //roa{A..Z}
                    for (i in 0 until 26) {
                        setTransition(106, i+65, 28)
                    }
                    //roa{a..z} / d
                    for (i in 0 until 26) {
                        if (i+97 == 100) continue // skip d
                        setTransition(106, i+97, 28)
                    }
                    //roa{0..9}
                    for (i in 0 until 10) {
                        setTransition(106, i+48, 29)
                    }

                    setTransition(106, 'd', 107)
                    //road{A..Z}
                    for (i in 0 until 26) {
                        setTransition(107, i+65, 28)
                    }
                    //road{a..z}
                    for (i in 0 until 26) {
                        setTransition(107, i+97, 28)
                    }
                    //road{0..9}
                    for (i in 0 until 10) {
                        setTransition(107, i+48, 29)
                    }

            //restaurant
                setTransition(100, 'e', 108)
                //re{A..Z}
                for (i in 0 until 26) {
                    setTransition(108, i+65, 28)
                }
                //re{a..z} / s
                for (i in 0 until 26) {
                    if (i+97 == 115) continue // skip s
                    setTransition(108, i+97, 28)
                }
                //re{0..9}
                for (i in 0 until 10) {
                    setTransition(108, i+48, 29)
                }

                setTransition(108, 's', 109)
                //res{A..Z}
                for (i in 0 until 26) {
                    setTransition(109, i+65, 28)
                }
                //res{a..z} / t
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(109, i+97, 28)
                }
                //res{0..9}
                for (i in 0 until 10) {
                    setTransition(109, i+48, 29)
                }

                setTransition(109, 't', 110)
                //rest{A..Z}
                for (i in 0 until 26) {
                    setTransition(110, i+65, 28)
                }
                //rest{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(110, i+97, 28)
                }
                //rest{0..9}
                for (i in 0 until 10) {
                    setTransition(110, i+48, 29)
                }

                setTransition(110, 'a', 111)
                //resta{A..Z}
                for (i in 0 until 26) {
                    setTransition(111, i+65, 28)
                }
                //resta{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 117) continue // skip u
                    setTransition(111, i+97, 28)
                }
                //resta{0..9}
                for (i in 0 until 10) {
                    setTransition(111, i+48, 29)
                }

                setTransition(111, 'u', 112)
                //restau{A..Z}
                for (i in 0 until 26) {
                    setTransition(112, i+65, 28)
                }
                //restau{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 114) continue // skip r
                    setTransition(112, i+97, 28)
                }
                //restau{0..9}
                for (i in 0 until 10) {
                    setTransition(112, i+48, 29)
                }

                setTransition(112, 'r', 113)
                //restaur{A..Z}
                for (i in 0 until 26) {
                    setTransition(113, i+65, 28)
                }
                //restaur{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 97) continue // skip a
                    setTransition(113, i+97, 28)
                }
                //restaur{0..9}
                for (i in 0 until 10) {
                    setTransition(113, i+48, 29)
                }

                setTransition(113, 'a', 114)
                //restaura{A..Z}
                for (i in 0 until 26) {
                    setTransition(114, i+65, 28)
                }
                //restaura{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 110) continue // skip n
                    setTransition(114, i+97, 28)
                }
                //restaura{0..9}
                for (i in 0 until 10) {
                    setTransition(114, i+48, 29)
                }

                setTransition(114, 'n', 115)
                //restauran{A..Z}
                for (i in 0 until 26) {
                    setTransition(115, i+65, 28)
                }
                //restauran{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 116) continue // skip t
                    setTransition(115, i+97, 28)
                }
                //restauran{0..9}
                for (i in 0 until 10) {
                    setTransition(115, i+48, 29)
                }

                setTransition(115, 't', 116)
                //restaurant{A..Z}
                for (i in 0 until 26) {
                    setTransition(116, i+65, 28)
                }
                //restaurant{a..z}
                for (i in 0 until 26) {
                    setTransition(116, i+97, 28)
                }
                //restaurant{0..9}
                for (i in 0 until 10) {
                    setTransition(116, i+48, 29)
                }

            //radius
                setTransition(100, 'a', 122)
                //ra{A..Z}
                for (i in 0 until 26) {
                    setTransition(122, i+65, 28)
                }
                //ra{a..z} / d
                for (i in 0 until 26) {
                    if (i+97 == 100) continue // skip d
                    setTransition(122, i+97, 28)
                }
                //ra{0..9}
                for (i in 0 until 10) {
                    setTransition(122, i+48, 29)
                }

                setTransition(122, 'd', 123)
                //rad{A..Z}
                for (i in 0 until 26) {
                    setTransition(123, i+65, 28)
                }
                //rad{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 105) continue // skip i
                    setTransition(123, i+97, 28)
                }
                //rad{0..9}
                for (i in 0 until 10) {
                    setTransition(123, i+48, 29)
                }

                setTransition(123, 'i', 124)
                //radi{A..Z}
                for (i in 0 until 26) {
                    setTransition(124, i+65, 28)
                }
                //radi{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 117) continue // skip u
                    setTransition(124, i+97, 28)
                }
                //radi{0..9}
                for (i in 0 until 10) {
                    setTransition(124, i+48, 29)
                }

                setTransition(124, 'u', 125)
                //radiu{A..Z}
                for (i in 0 until 26) {
                    setTransition(125, i+65, 28)
                }
                //radiu{a..z}
                for (i in 0 until 26) {
                    if (i+97 == 115) continue // skip s
                    setTransition(125, i+97, 28)
                }
                //radiu{0..9}
                for (i in 0 until 10) {
                    setTransition(125, i+48, 29)
                }

                setTransition(125, 's', 126)
                //radius{A..Z}
                for (i in 0 until 26) {
                    setTransition(126, i+65, 28)
                }
                //radius{a..z}
                for (i in 0 until 26) {
                    setTransition(126, i+97, 28)
                }
                //radius{0..9}
                for (i in 0 until 10) {
                    setTransition(126, i+48, 29)
                }

        //______________________________________
        //{A..Z}+
        for (i in 0 until 26) {
            setTransition(28, i+65, 28)
        }
        //{A..Za..z}+
        for (i in 0 until 26) {
            setTransition(28, i+97, 28)
        }
        //{A..Za..z}+{0..9}
        for (i in 0 until 10) {
            setTransition(28, i+48, 29)
        }
        //{A..Za..z}+{0..9}+
        for (i in 0 until 10) {
            setTransition(29, i+48, 29)
        }
        //{A..Za..z_}+{0..9}+
        setTransition(28, '_', 28)

        // =
        setTransition(1, '=', 117)
        // .
        setTransition(1, '.', 118)

        //______________________________________
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
        setSymbol(21, Symbol.INTEGERDIVIDES)
        setSymbol(22, Symbol.POW)
        setSymbol(23, Symbol.SKIP)
        setSymbol(24, Symbol.SKIP)
        setSymbol(25, Symbol.SKIP)
        setSymbol(26, Symbol.SKIP)
        setSymbol(27, Symbol.EOF)
        setSymbol(117, Symbol.EQUALS)
        setSymbol(118, Symbol.DOT)

        setSymbol(28, Symbol.VARIABLE)
        setSymbol(29, Symbol.VARIABLE)

        setSymbol(30, Symbol.VARIABLE)
        setSymbol(31, Symbol.VARIABLE)
        setSymbol(32, Symbol.VARIABLE)
        setSymbol(33, Symbol.VARIABLE)
        setSymbol(34, Symbol.VARIABLE)
        setSymbol(35, Symbol.MARKER)

        setSymbol(36, Symbol.VARIABLE)
        setSymbol(37, Symbol.VARIABLE)
        setSymbol(38, Symbol.VARIABLE)
        setSymbol(39, Symbol.BEND)

        setSymbol(40, Symbol.VARIABLE)
        setSymbol(41, Symbol.VARIABLE)
        setSymbol(42, Symbol.VARIABLE)
        setSymbol(43, Symbol.VARIABLE)
        setSymbol(44, Symbol.VARIABLE)
        setSymbol(45, Symbol.VARIABLE)
        setSymbol(46, Symbol.VARIABLE)
        setSymbol(47, Symbol.VARIABLE)
        setSymbol(48, Symbol.HIGHLIGHT)

        setSymbol(49, Symbol.VARIABLE)
        setSymbol(50, Symbol.VARIABLE)
        setSymbol(51, Symbol.VARIABLE)
        setSymbol(52, Symbol.VARIABLE)
        setSymbol(53, Symbol.VARIABLE)
        setSymbol(54, Symbol.VARIABLE)
        setSymbol(55, Symbol.FOREACH)

        setSymbol(56, Symbol.VARIABLE)
        setSymbol(57, Symbol.IN)

        setSymbol(74, Symbol.STRING)

        setSymbol(60, Symbol.VARIABLE)
        setSymbol(61, Symbol.VARIABLE)
        setSymbol(62, Symbol.VARIABLE)
        setSymbol(63, Symbol.VARIABLE)
        setSymbol(64, Symbol.POINT)

        setSymbol(65, Symbol.VARIABLE)
        setSymbol(66, Symbol.VARIABLE)
        setSymbol(67, Symbol.VARIABLE)
        setSymbol(68, Symbol.PRINT)

        setSymbol(69, Symbol.VARIABLE)
        setSymbol(70, Symbol.VARIABLE)
        setSymbol(71, Symbol.VARIABLE)
        setSymbol(72, Symbol.VARIABLE)
        setSymbol(73, Symbol.TYPE_COORDINATE)

        setSymbol(75, Symbol.VARIABLE)
        setSymbol(76, Symbol.VARIABLE)
        setSymbol(77, Symbol.CONST)

        setSymbol(78, Symbol.VARIABLE)
        setSymbol(79, Symbol.VARIABLE)
        setSymbol(80, Symbol.CITY)

        setSymbol(81, Symbol.VARIABLE)
        setSymbol(82, Symbol.VARIABLE)
        setSymbol(83, Symbol.VARIABLE)
        setSymbol(84, Symbol.NAME)

        setSymbol(119, Symbol.VARIABLE)
        setSymbol(120, Symbol.TYPE_NUMBER)

        setSymbol(121, Symbol.VARIABLE)
        setSymbol(85, Symbol.VARIABLE)
        setSymbol(86, Symbol.VARIABLE)
        setSymbol(87, Symbol.VARIABLE)
        setSymbol(88, Symbol.SHAPE)

        setSymbol(89, Symbol.VARIABLE)
        setSymbol(90, Symbol.VARIABLE)
        setSymbol(91, Symbol.VARIABLE)
        setSymbol(92, Symbol.VARIABLE)
        setSymbol(93, Symbol.TYPE_STRING)

        setSymbol(94, Symbol.VARIABLE)
        setSymbol(95, Symbol.VARIABLE)
        setSymbol(96, Symbol.VARIABLE)
        setSymbol(97, Symbol.TYPE_LIST)

        setSymbol(98, Symbol.VARIABLE)
        setSymbol(99, Symbol.LINE)

        setSymbol(100, Symbol.VARIABLE)
        setSymbol(101, Symbol.VARIABLE)
        setSymbol(102, Symbol.VARIABLE)
        setSymbol(103, Symbol.VARIABLE)
        setSymbol(104, Symbol.VARIABLE)
        setSymbol(105, Symbol.ROUTES)

        setSymbol(106, Symbol.VARIABLE)
        setSymbol(107, Symbol.ROAD)

        setSymbol(108, Symbol.VARIABLE)
        setSymbol(109, Symbol.VARIABLE)
        setSymbol(110, Symbol.VARIABLE)
        setSymbol(111, Symbol.VARIABLE)
        setSymbol(112, Symbol.VARIABLE)
        setSymbol(113, Symbol.VARIABLE)
        setSymbol(114, Symbol.VARIABLE)
        setSymbol(115, Symbol.VARIABLE)
        setSymbol(116, Symbol.RESTAURANT)

        setSymbol(117, Symbol.EQUALS)
        setSymbol(118, Symbol.DOT)

        setSymbol(122, Symbol.VARIABLE)
        setSymbol(123, Symbol.VARIABLE)
        setSymbol(124, Symbol.VARIABLE)
        setSymbol(125, Symbol.VARIABLE)
        setSymbol(126, Symbol.RADIUS)
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

// za izpis tokenov

fun name(symbol: Symbol) =
    when (symbol) {
        Symbol.CONST -> "const"
        Symbol.VARIABLE -> "variable"
        Symbol.EQUALS -> "equals"
        Symbol.TYPE_NUMBER -> "type_number"
        Symbol.TYPE_STRING -> "type_string"
        Symbol.TYPE_COORDINATE -> "type_coordinate"
        Symbol.TYPE_LIST -> "type_list"
        Symbol.LTHAN -> "lthan"
        Symbol.MTHAN -> "mthan"
        Symbol.CITY -> "city"
        Symbol.BLOCK_START -> "block_start"
        Symbol.BLOCK_END -> "block_end"
        Symbol.NAME -> "name"
        Symbol.COLON -> "colon"
        Symbol.STRING -> "string"
        Symbol.SHAPE -> "shape"
        Symbol.LINE -> "line"
        Symbol.LSQ_BRACKET -> "lsq_bracket"
        Symbol.COMMA -> "comma"
        Symbol.RSQ_BRACKET -> "rsq_bracket"
        Symbol.LBRACKET -> "lbracket"
        Symbol.RBRACKET -> "rbracket"
        Symbol.MARKER -> "marker"
        Symbol.POINT -> "point"
        Symbol.ROUTES -> "routes"
        Symbol.ROAD -> "road"
        Symbol.BEND -> "bend"
        Symbol.FOREACH -> "foreach"
        Symbol.HIGHLIGHT -> "highlight"
        Symbol.SEMICOL -> "semicol"
        Symbol.HASH -> "hash"
        Symbol.REAL -> "real"
        Symbol.PLUS -> "plus"
        Symbol.MINUS -> "minus"
        Symbol.TIMES -> "times"
        Symbol.DIVIDES -> "divide"
        Symbol.INTEGERDIVIDES -> "integer-divide"
        Symbol.POW -> "pow"
        Symbol.PRINT -> "print"
        Symbol.IN -> "in"
        Symbol.DOT -> "dot"
        Symbol.RESTAURANT -> "restaurant"
        else -> throw Error("Invalid symbol")
    }
fun printTokens(scanner: Scanner, output: FileOutputStream) {
    val writer = output.writer(Charsets.UTF_8)

    var token = scanner.getToken()
    while (token.symbol != Symbol.EOF) {
        writer.append("${name(token.symbol)}(\"${token.lexeme}\") ") // The output ends with a space!
        token = scanner.getToken()
    }

    writer.appendLine()
    writer.flush()
}


// BNF:
/*
Main ::= Main_Program
Main_Program ::= Main_Program’’ ; Main_Program’
Main_Program’ ::= Main_Program
Main_Program’ ::= ''
Main_Program’’ ::= Declaration
Main_Program’’ ::= Assignment
Main_Program’’ ::= City
Declaration ::= const Declaration’
Declaration ::= Declaration’
Declaration’ ::= coord spr = Coord
Declaration’ ::= string spr = String
Declaration’ ::= num spr = Expression
Declaration’ ::= List spr = ListContent
DeclarationList’ ::= Type
DeclarationList’ ::= restaurant
DeclarationList’ ::= city
Assignment ::= # spr = Assignment’
Assignment’ ::= coord Coord
Assignment’ ::= num Expression
Assignment’ ::= string String
Assignment’ ::= list ListContent
List ::= list < DeclarationList’ >
ListContent ::= [ Inner_List ]
Inner_List ::= Expression Inner_List’
Inner_List ::= ''
Inner_List’ ::= , Expression Inner_List’
Inner_List’ ::= ''
Type ::= num
Type ::= string
Type ::= coord
Expression ::= Additive
Additive ::= Multiplicative Additive’
Additive’ ::= + Multiplicative Additive’
Additive’ ::= - Multiplicative Additive’
Additive’ ::= ''
Multiplicative ::= Exponential Multiplicative’
Multiplicative’ ::= * Exponential Multiplicative’
Multiplicative’ ::= / Exponential Multiplicative’
Multiplicative’ ::= // Exponential Multiplicative’
Multiplicative’ ::= ''
Exponential ::= Unary Exponential’
Exponential’ ::= ^ Unary Exponential’
Exponential’ ::= ''
Unary ::= + Primary
Unary ::= - Primary
Unary ::= Primary
Primary ::= 0
Primary ::= spr
Primary ::= ( Additive )
City ::= city spr { City’ }
City’ ::= City’’’ ; City’’
City’’ ::= City’
City’’ ::= ''
City’’’ ::= City_Constructs
City’’’ ::= Declaration
City’’’ ::= Assignment
City’’’ ::= Print
City’’’ ::= For
Print ::= print ( Expression )
City_Constructs ::= Restaurant
City_Constructs ::= Road
Restaurant ::= restaurant spr { Name Shape Marker Routes }
Name ::= name : String
String ::= spr
String ::= a
Shape ::= shape : { Lines }
Lines ::= # Line # Line # Line Line’
Line’ ::= # Line Line’
Line’ ::= ''
Line ::= line [ Coord , Coord ]
Coord ::= spr
Coord ::= ( Expression , Expression )
Marker ::= Marker’
Marker ::= ''
Marker’ ::= marker : Point
Point ::= point [ Coord ]
Routes ::= Routes’
Routes ::= ''
Routes’ ::= routes : [ Roads ]
Roads ::= Road Road’
Road’ ::= , Road Road’
Road’ ::= ''
Road ::= road spr { Name Road_Shapes }
Road_Shapes ::= shape : { Road_Shapes’ Road_Shapes }
Road_Shapes ::= ''
Road_Shapes’ ::= # Road_Shape Road_Shapes’
Road_Shapes’ ::= ''
Road_Shape ::= Line
Road_Shape ::= Bend
Bend ::= bend [ Coord , Coord , Expression ]
For ::= foreach spr in Variable { Program }
Variable ::= spr Variable’
Variable ::= Radius
Variable’ ::= . Variable’’
Variable’ ::= ''
Variable’’ ::= spr
Variable’’ ::= routes
Radius ::= radius [ Coord , Expression ]
Program ::= Program’’ ; Program’
Program’ ::= Program ; Program’
Program’ ::= ''
Program’’ ::= Declaration
Program’’ ::= Assignment
Program’’ ::= Print
Program’’ ::= Highlight
Highlight ::= highlight ( spr )
*/

/*
interface Block {
    fun toGeoJSON(level: Int): String
    fun tabulators(level: Int): String {
        var tabs = ""
        for (i in 0 until level) {
            tabs += "\t"
        }
        return tabs
    }
}

class Main {
    private val blocks = mutableListOf<Block>()
    fun addBlock(block: Block) {
        block.tabulators(2)
        blocks.add(block)
    }
    fun toGeoJSON(): String {
        return "{\n" +
                    "\t\"type\": \"FeatureCollection\"," +
                    "\t\"features\": [\n" +
                        blocks.joinToString("") +
                    "\n\t]\n" +
                "\n}"
    }
}

class MainProgram : Block {
    private val blocks = mutableListOf<Block>()
    override fun toGeoJSON(level: Int): String {
        return MainProgramPrimePrime.toGeoJSON(level) +
                ";" +
                MainProgramPrime.toGeoJSON(level)
    }
}

class MainProgramPrime : Block {
    private val blocks = mutableListOf<Block>()
    override fun toGeoJSON(level: Int): String {
        return MainProgram.toGeoJSON(level)
    }
}
 */

typealias Env = Map<String, Double>
data class Result(val env: Env, val value: Real?, val trace: List<Double>)

interface MAIN_PROGRAM_PRIME_PRIME {
    fun toGeoJSON(indent: Int = 0): String
}

interface ROAD_SHAPE_INTERFACE {
    fun toGeoJSON(indent: Int): String
}

class MainProgram(private val left: MAIN_PROGRAM_PRIME_PRIME, private val right: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "$left\n$right"
    }

    override fun toGeoJSON(indent: Int): String {
        return "${left.toGeoJSON()}\n${right.toGeoJSON()}"
    }
}

class City(private val variable: Variable, private val cityInside: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "$cityInside"
    }

    override fun toGeoJSON(indent: Int): String {
        var string =
            "\t".repeat(indent) + "{\n" +
            "\t".repeat(indent + 1) + "\"type\": \"FeatureCollection\",\n" +
            "\t".repeat(indent + 1) + "\"features\": [\n"

        string += cityInside.toGeoJSON(indent + 2)

        string += "\t".repeat(indent + 1) + "]\n" +
            "\t".repeat(indent) + "}"

        return string
    }
}

class CityPrime(private val left: MAIN_PROGRAM_PRIME_PRIME, private val right: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "$left\n$right"
    }
    //tu pomoje morš dat is restaurant
    override fun toGeoJSON(indent: Int): String {
        return "${left.toGeoJSON(indent)},\n${right.toGeoJSON(indent)}"
    }
}

class Restaurant(private val name: String, private val shape: MAIN_PROGRAM_PRIME_PRIME, private val marker: Point, private val routes: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "Name:$name\nShape:$shape\nMarker:$marker\nRoutes:$routes"
    }

    override fun toGeoJSON(indent: Int): String {
        var string = "\t".repeat(indent) + "{\n" +
                     "\t".repeat(indent + 1) + "\"type\": \"Feature\",\n" +
                     "\t".repeat(indent + 1) + "\"properties\": {\n"

        string +=    "\t".repeat(indent + 2) + "\"name\": $name\n"

        string +=    "\t".repeat(indent + 1) + "},\n"

        string +=    "${shape.toGeoJSON(indent + 1)}\n"

        string +=    "\t".repeat(indent) + "},\n"

        val routesGEOJson = routes.toGeoJSON(indent)

        string +=    "${marker.toGeoJSON(indent)}${if (routesGEOJson.isBlank()) "" else ","}\n"

        string +=    routesGEOJson
        return string
    }
}

class Coords(private val coords: MutableList<Coord>): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        var string = ""
        for(coord in coords) {
            string += "$coord"
        }
        return string
    }

    override fun toGeoJSON(indent: Int): String {
        var string = "\t".repeat(indent) + "\"geometry\": {\n" +
                     "\t".repeat(indent + 1) + "\"coordinates\": [\n" +
                     "\t".repeat(indent + 2) + "[\n"

        for(coord in coords) {
            string += "${coord.toGeoJSON(indent + 3)},\n"
        }

        string += "${coords[0].toGeoJSON(indent + 3)}\n"

        string +=    "\t".repeat(indent + 2) + "]\n" +
                     "\t".repeat(indent + 1) + "],\n" +
                     "\t".repeat(indent + 1) + "\"type\": \"Polygon\"\n" +
                     "\t".repeat(indent) + "}"

        return string
    }
}

class Line(private val left: Coord, private val right: Coord): MAIN_PROGRAM_PRIME_PRIME, ROAD_SHAPE_INTERFACE {
    override fun toString(): String {
        return "Line: [ $left , $right]"
    }

    override fun toGeoJSON(indent: Int): String {
        var string = "\t".repeat(indent) + "\"geometry\": {\n" +
                     "\t".repeat(indent + 1) + "\"coordinates\": [\n"

        string +=    "${left.toGeoJSON(indent + 2)},\n" +
                     "${right.toGeoJSON(indent + 2)}\n"

        string +=    "\t".repeat(indent + 1) + "],\n" +
                     "\t".repeat(indent + 1) + "\"type\": \"LineString\"\n" +
                     "\t".repeat(indent) + "}\n"

        return string
    }
}

class Coord(val left: Expr, val right: Expr): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "Coord ($left, $right)"
    }

    //tu boš mogo left.eval pa right.eval uporabit se mi zdi
    override fun toGeoJSON(indent: Int): String {
        var string = "\t".repeat(indent) + "[\n"

            string += "\t".repeat(indent + 1) + "${left.eval(emptyMap())},\n" +
                      "\t".repeat(indent + 1) + "${right.eval(emptyMap())}\n"

        string +=    "\t".repeat(indent) + "]"

        return string
    }
}

class Point(private val coord: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "point [ $coord ]"
    }

    //tu kličeš coord.toGeoJSON samo morš še dodat, da se pokaže kot marker
    override fun toGeoJSON(indent: Int): String {
        var string = "\t".repeat(indent) + "{\n" +
                     "\t".repeat(indent + 1) + "\"type\": \"Feature\",\n" +
                     "\t".repeat(indent + 1) + "\"properties\": {\n" +
                     "\t".repeat(indent + 2) + "\"name\": \"Marker\"\n" +
                     "\t".repeat(indent + 1) + "},\n" +
                     "\t".repeat(indent + 1) + "\"geometry\": {\n" +
                     "\t".repeat(indent + 2) + "\"type\": \"Point\",\n" +
                     "\t".repeat(indent + 2) + "\"coordinates\":\n"

        string += "${coord.toGeoJSON(indent + 3)}\n"

        string += "\t".repeat(indent + 1) + "}\n" +
                  "\t".repeat(indent) + "}"

        return string
    }
}

class Routes(private val roads: MutableList<Road>): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        var string = ""
        for(road in roads) {
            string += "$road"
        }
        return string
    }

    override fun toGeoJSON(indent: Int): String {
        if(roads.size == 0) {
            return ""
        } else {
            var string = ""
            for(road in roads) {
                string += road.toGeoJSON(indent)
            }

            string = string.dropLast(2)
            string += "\n"

            return string
        }
    }
}

class Road(private val name: String, private val roadShapes: MutableList<RoadShape>): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "Road { Name: $name Shape: $roadShapes }"
    }

    override fun toGeoJSON(indent: Int): String {
        var string = "";

        for(roadShape in roadShapes) {
            string += "\t".repeat(indent) + "{\n" +
                      "\t".repeat(indent + 1) + "\"type\": \"Feature\",\n" +
                      "\t".repeat(indent + 1) + "\"properties\": {\n" +
                      "\t".repeat(indent + 2) + "\"name\": $name\n" +
                      "\t".repeat(indent + 1) + "},\n"

            string += roadShape.toGeoJSON(indent + 1)

            string += "\t".repeat(indent) + "},\n"
        }

        return string
    }
}

class RoadShape(private val roadShape: ROAD_SHAPE_INTERFACE): MAIN_PROGRAM_PRIME_PRIME {
    override fun toString(): String {
        return "roadShape: $roadShape"
    }

    override fun toGeoJSON(indent: Int): String {
        var string = ""
        if(roadShape is Line) {
            return roadShape.toGeoJSON(indent)
        } else if (roadShape is Bend) {
            return roadShape.toGeoJSON(indent)
        } else {
            throw Exception("${roadShape::class.simpleName} is not a valid class for this operation")
        }
    }
}

class Bend(private val left: Coord, private val right: Coord, private val bendAmount: Expr): MAIN_PROGRAM_PRIME_PRIME, ROAD_SHAPE_INTERFACE {
    override fun toString(): String {
        return "Bend: [ $left , $right, $bendAmount]"
    }

    override fun toGeoJSON(indent: Int): String {
        var string = "\t".repeat(indent) + "\"geometry\": {\n" +
                "\t".repeat(indent + 1) + "\"coordinates\": [\n"

        if(bendAmount.eval(emptyMap()) == 0.0) {
            string += "${left.toGeoJSON(indent + 2)},\n" +
                      "${right.toGeoJSON(indent + 2)}\n"
        } else {
            //string += "${right.toGeoJSON(indent + 2)},\n"

            val listOfCoords = generateCurvePoints(10);
            for (coord in listOfCoords) {
                string += "${coord.toGeoJSON(indent + 2)},\n"
            }

            string = string.dropLast(2)
            string += "\n"

            //string += "${left.toGeoJSON(indent + 2)}\n"
        }

        string +=    "\t".repeat(indent + 1) + "],\n" +
                     "\t".repeat(indent + 1) + "\"type\": \"LineString\"\n" +
                     "\t".repeat(indent) + "}\n"

        return string
    }

    fun generateCurvePoints(numPoints: Int): List<Coord> {
        val leftX = left.left.eval(emptyMap())
        val leftY = left.right.eval(emptyMap())
        val rightX = right.left.eval(emptyMap())
        val rightY = right.right.eval(emptyMap())

        val relativeAngle = Math.toRadians(bendAmount.eval(emptyMap()))
        val oppositeRelativeAngle = PI - relativeAngle

        val angle = angleBetween(leftX, leftY, rightX, rightY)
        val dist = distanceBetween(leftX, leftY, rightX, rightY)
        val constant = (4 / 3) * tan(PI / 8)

        val c0 = offsetCoordinate(leftX, leftY, constant * dist, angle + relativeAngle)
        val c1 = offsetCoordinate(rightX, rightY, constant * dist, angle + oppositeRelativeAngle)

        val ps = mutableListOf<Coord>()
        for (i in 0 .. numPoints) {
            val t = i / numPoints.toDouble()
            ps.add(at(t, leftX, leftY, c0.left.eval(emptyMap()), c0.right.eval(emptyMap()), c1.left.eval(emptyMap()), c1.right.eval(emptyMap()), rightX, rightY))
        }
        return ps
    }

    fun at(t: Double,
           firstX: Double, firstY: Double,
           secondX: Double, secondY: Double,
           thirdX: Double, thirdY: Double,
           fourthX: Double, fourthY: Double): Coord {

        val oneMinusT = 1.0 - t
        val oneMinusTSquared = oneMinusT * oneMinusT
        val tSquared = t * t

        val term1X = firstX * oneMinusT * oneMinusT * oneMinusT
        val term1Y = firstY * oneMinusT * oneMinusT * oneMinusT

        val term2X = secondX * 3.0 * oneMinusTSquared * t
        val term2Y = secondY * 3.0 * oneMinusTSquared * t

        val term3X = thirdX * 3.0 * oneMinusT * tSquared
        val term3Y = thirdY * 3.0 * oneMinusT * tSquared

        val term4X = fourthX * tSquared * t
        val term4Y = fourthY * tSquared * t

        val resultX = term1X + term2X + term3X + term4X
        val resultY = term1Y + term2Y + term3Y + term4Y

        return Coord(Real(resultX), Real(resultY))
    }


    fun angleBetween(leftX: Double, leftY: Double, rightX: Double, rightY: Double): Double {
        val dx = rightX - leftX
        val dy = rightY - leftY
        return atan2(dy, dx)
    }

    fun distanceBetween(leftX: Double, leftY: Double, rightX: Double, rightY: Double): Double {
        val dx = rightX - leftX
        val dy = rightY - leftY
        return sqrt(dx * dx + dy * dy)
    }

    fun offsetCoordinate(coordX: Double, coordY: Double, distance: Double, angle: Double): Coord {
        val newX = coordX + distance * cos(angle)
        val newY = coordY + distance * sin(angle)
        return Coord(Real(newX), Real(newY))
    }
}


interface Expr {
    fun eval(env: Env): Double
}

class Plus(private val left: Expr, private val right: Expr) : Expr {
    override fun toString(): String {
        return "($left + $right)"
    }

    override fun eval(env: Env): Double {
        return left.eval(env) + right.eval(env)
    }
}

class Minus(private val left: Expr, private val right: Expr) : Expr {
    override fun toString(): String {
        return "($left - $right)"
    }

    override fun eval(env: Env): Double {
        return left.eval(env) - right.eval(env)
    }
}

class Times(private val left: Expr, private val right: Expr) : Expr {
    override fun toString(): String {
        return "($left * $right)"
    }

    override fun eval(env: Env): Double {
        return left.eval(env) * right.eval(env)
    }
}

class Divides(private val left: Expr, private val right: Expr) : Expr {
    override fun toString(): String {
        return "($left / $right)"
    }

    override fun eval(env: Env): Double {
        return left.eval(env) / right.eval(env)
    }
}

class IntegerDivides(private val left: Expr, private val right: Expr) : Expr {
    override fun toString(): String {
        return "($left // $right)"
    }

    override fun eval(env: Env): Double {
        return (left.eval(env).toInt() / right.eval(env).toInt()).toDouble()
    }
}

class Pow(private val left: Expr, private val right: Expr) : Expr {
    override fun toString(): String {
        return "($left^$right)"
    }

    override fun eval(env: Env): Double {
        return left.eval(env).pow(right.eval(env))
    }
}

class UnaryPlus(private val expr: Expr) : Expr {
    override fun toString(): String {
        return "(+$expr)"
    }

    override fun eval(env: Env): Double {
        return +expr.eval(env)
    }
}

class UnaryMinus(private val expr: Expr) : Expr {
    override fun toString(): String {
        return "(-$expr)"
    }

    override fun eval(env: Env): Double {
        return -expr.eval(env)
    }
}

class Real(private val real: Double) : Expr {
    override fun toString(): String {
        return "$real"
    }

    override fun eval(env: Env): Double {
        return real
    }
}

class Variable(private val variable: String) : Expr {
    override fun toString(): String {
        return variable
    }

    override fun eval(env: Env): Double {
        return env[variable] ?: throw Error("Variable $variable doesn't exist")
    }
}




class ParseException(symbol: Symbol, lexeme: String, row: Int, column: Int) : Exception("PARSE ERROR (${name(symbol)}, $lexeme) at $row:$column")

class Parser(private val scanner: Scanner) {
    private var last: Token? = null

    private fun panic(): Nothing =
        last?.let { throw ParseException(it.symbol, it.lexeme, it.startRow, it.startColumn) } ?: error("cannot happen")

    fun MAIN(): MAIN_PROGRAM_PRIME_PRIME {
        last = scanner.getToken()
        val result = MAIN_PROGRAM()
        return when (last?.symbol) {
            Symbol.EOF -> result
            else -> panic()
        }
    }

    fun MAIN_PROGRAM(): MAIN_PROGRAM_PRIME_PRIME = MAIN_PROGRAM_PRIME(MAIN_PROGRAM_PRIME_PRIME())

    fun MAIN_PROGRAM_PRIME(prev: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME =
        when (last?.symbol) {
            Symbol.CONST, Symbol.TYPE_COORDINATE, Symbol.TYPE_STRING, Symbol.TYPE_NUMBER, Symbol.TYPE_LIST, Symbol.HASH, Symbol.CITY -> MainProgram(prev, MAIN_PROGRAM_PRIME(MAIN_PROGRAM()))
            Symbol.EOF -> prev
            else -> panic()
        }

    fun MAIN_PROGRAM_PRIME_PRIME(): MAIN_PROGRAM_PRIME_PRIME {
        return when (last?.symbol) {
            /*Symbol.CONST, Symbol.TYPE_COORDINATE, Symbol.TYPE_STRING, Symbol.TYPE_NUMBER, Symbol.TYPE_LIST -> {
                val declaration = DECLARATION()
                parseTerminal(Symbol.SEMICOL)
                declaration
            }
            Symbol.HASH -> {
                val assignment = ASSIGNMENT()
                parseTerminal(Symbol.SEMICOL)
                assignment
            } */
            Symbol.CITY -> {
                val city = CITY()
                parseTerminal(Symbol.SEMICOL)
                city
            }
            else -> panic()
        }
    }

    /*fun DECLARATION(): MAIN_PROGRAM_PRIME_PRIME {
        return when (last?.symbol) {
            Symbol.CONST -> {
                parseTerminal(Symbol.CONST)
                val declaration = DECLARATION_PRIME()
                declaration
            }
            Symbol.TYPE_COORDINATE, Symbol.TYPE_STRING, Symbol.TYPE_NUMBER, Symbol.TYPE_LIST -> DECLARATION_PRIME()
            else -> panic()
        }
    }

    fun DECLARATION_PRIME(): MAIN_PROGRAM_PRIME_PRIME {
        return when (last?.symbol) {
            Symbol.TYPE_COORDINATE -> {
                parseTerminal(Symbol.TYPE_COORDINATE)
                val variable = Variable(parseTerminal(Symbol.VARIABLE)) //tip coord
                parseTerminal(Symbol.EQUALS)
                val coord = COORD()
                Declaration(variable, coord)
            }
            Symbol.TYPE_STRING -> {
                parseTerminal(Symbol.TYPE_STRING)
                val variable = Variable(parseTerminal(Symbol.VARIABLE)) //tip string
                parseTerminal(Symbol.EQUALS)
                val string = STRING()
                Declaration(variable, string)
            }
            Symbol.TYPE_NUMBER -> {
                parseTerminal(Symbol.TYPE_NUMBER)
                val variable = Variable(parseTerminal(Symbol.VARIABLE)) //tip string
                parseTerminal(Symbol.EQUALS)
                val expr = parseAdditive()
                Declaration(variable, expr)
            }
            Symbol.TYPE_LIST -> {
                val listType = LIST() //tip list
                parseTerminal(Symbol.EQUALS)
                val listContent = LISTCONTENT()
                Declaration(listType, listContent) /*TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
            }
            else -> panic()
        }
    }

    fun ASSIGNMENT(): MAIN_PROGRAM_PRIME_PRIME {
        return when (last?.symbol) {
            Symbol.HASH -> {
                parseTerminal(Symbol.HASH)
                val variable = Variable(parseTerminal(Symbol.VARIABLE))
                parseTerminal(Symbol.EQUALS)
                val right = ASSIGNMENT_PRIME()
                Assignment(variable, right)
            }
            else -> panic()
        }
    }

    fun ASSIGNMENT_PRIME(): MAIN_PROGRAM_PRIME_PRIME {
        return when (last?.symbol) {
            Symbol.TYPE_COORDINATE -> {
                parseTerminal(Symbol.TYPE_COORDINATE)
                val coord = COORD()
                coord
            }
            Symbol.TYPE_NUMBER -> {
                parseTerminal(Symbol.TYPE_NUMBER)
                val number = parseAdditive()
                number
            }
            Symbol.TYPE_STRING -> {
                parseTerminal(Symbol.TYPE_STRING)
                val string = STRING()
                string
            }
            Symbol.TYPE_LIST -> {
                /*TODO*/
            }
            else -> panic()
        }
    }

        fun LIST(): Boolean {
            if (last?.symbol == Symbol.TYPE_LIST) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.LTHAN) {
                    last = scanner.getToken()
                    if (DECLARATION_LIST_PRIME()) {
                        if (last?.symbol == Symbol.MTHAN) {
                            last = scanner.getToken()
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun LIST_CONTENT(): Boolean {
            if (last?.symbol == Symbol.LSQ_BRACKET) {
                last = scanner.getToken()
                if (INNER_LIST()) {
                    if (last?.symbol == Symbol.RSQ_BRACKET) {
                        last = scanner.getToken()
                        return true
                    }
                }
            }
            return false
        }

        fun INNER_LIST(): Boolean {
            if (EXPRESSION()) {
                return INNER_LIST_PRIME()
            }
            return true
        }

        fun INNER_LIST_PRIME(): Boolean {
            if (last?.symbol == Symbol.COMMA) {
                last = scanner.getToken()
                if (EXPRESSION()) {
                    return INNER_LIST_PRIME()
                }
            }
            return true
        }

        fun TYPE(): Boolean {
            return when (last?.symbol) {
                Symbol.TYPE_NUMBER -> {
                    last = scanner.getToken()
                    true
                }
                Symbol.TYPE_STRING -> {
                    last = scanner.getToken()
                    true
                }
                Symbol.TYPE_COORDINATE -> {
                    last = scanner.getToken()
                    true
                }
                else -> false
            }
        }

        fun EXPRESSION(): Expr {
            return ADDITIVE()
        } */

        fun CITY(): MAIN_PROGRAM_PRIME_PRIME {
            return when (last?.symbol) {
                Symbol.CITY -> {
                    parseTerminal(Symbol.CITY)
                    val variable = Variable(parseTerminal(Symbol.VARIABLE))
                    parseTerminal(Symbol.BLOCK_START)
                    val cityInside = CITY_PRIME()
                    parseTerminal(Symbol.BLOCK_END)
                    City(variable, cityInside)
                }
                else -> panic()
            }
        }

        fun CITY_PRIME(): MAIN_PROGRAM_PRIME_PRIME = CITY_PRIME_PRIME(RESTAURANT())

        fun CITY_PRIME_PRIME(prev: MAIN_PROGRAM_PRIME_PRIME): MAIN_PROGRAM_PRIME_PRIME =
            when (last?.symbol) {
                Symbol.RESTAURANT -> CityPrime(prev, CITY_PRIME_PRIME(CITY_PRIME()))
                Symbol.BLOCK_END -> prev
                else -> panic()
            }

        fun RESTAURANT(): MAIN_PROGRAM_PRIME_PRIME {
            return when (last?.symbol) {
                Symbol.RESTAURANT -> {
                    parseTerminal(Symbol.RESTAURANT)
                    val variable = Variable(parseTerminal(Symbol.VARIABLE))
                    parseTerminal(Symbol.BLOCK_START)
                    val name = NAME()
                    val shape = SHAPE()
                    val marker = MARKER()
                    val routes = ROUTES()
                    parseTerminal(Symbol.BLOCK_END)
                    Restaurant(name, shape, marker, routes)
                }
                else -> panic()
            }
        }

        fun NAME(): String {
            return when (last?.symbol) {
                Symbol.NAME -> {
                    parseTerminal(Symbol.NAME)
                    parseTerminal(Symbol.COLON)
                    val string = STRING()
                    string
                }
                else -> panic()
            }
        }

        fun STRING(): String {
            return when (last?.symbol) {
//                Symbol.VARIABLE -> {
//                    val variable = Variable(parseTerminal(Symbol.VARIABLE))
//                    variable
//                }
                Symbol.STRING -> parseTerminal(Symbol.STRING)
                else -> panic()
            }
        }

        fun SHAPE(): MAIN_PROGRAM_PRIME_PRIME {
            return when (last?.symbol) {
                Symbol.SHAPE -> {
                    parseTerminal(Symbol.SHAPE)
                    parseTerminal(Symbol.COLON)
                    parseTerminal(Symbol.BLOCK_START)
                    val coords = COORDS()
                    parseTerminal(Symbol.BLOCK_END)
                    coords
                }
                else -> panic()
            }
        }

        fun COORDS(): MAIN_PROGRAM_PRIME_PRIME {
            val coords = mutableListOf<Coord>()

            while(last?.symbol == Symbol.LBRACKET) {
                val currentCoord = COORD()
                parseTerminal(Symbol.SEMICOL)
                coords.add(currentCoord)
            }

            if (coords.size < 3) {
                throw Exception("There should be atleast 3 coords")
            }
            return Coords(coords)
        }

        fun COORD(): Coord {
            return when (last?.symbol) {
                /*Symbol.VARIABLE -> {
                    val variable = Variable(parseTerminal(Symbol.VARIABLE))
                    variable
                } */
                Symbol.LBRACKET -> {
                    parseTerminal(Symbol.LBRACKET)
                    val leftExpr = parseAdditive()
                    parseTerminal(Symbol.COMMA)
                    val rightExpr = parseAdditive()
                    parseTerminal(Symbol.RBRACKET)
                    Coord(leftExpr, rightExpr)
                }
                else -> panic()
            }
        }

        fun MARKER(): Point {
            return when (last?.symbol) {
                Symbol.MARKER -> {
                    parseTerminal(Symbol.MARKER)
                    parseTerminal(Symbol.COLON)
                    val point = POINT()
                    point
                }
                /*Symbol.ROUTES, Symbol.BLOCK_END -> {

                } */
                else -> panic()
            }
        }

        fun POINT(): Point {
            return when (last?.symbol) {
                Symbol.POINT -> {
                    parseTerminal(Symbol.POINT)
                    parseTerminal(Symbol.LSQ_BRACKET)
                    val coord = COORD()
                    parseTerminal(Symbol.RSQ_BRACKET)
                    Point(coord)
                }
                else -> panic()
            }
        }

        fun ROUTES(): MAIN_PROGRAM_PRIME_PRIME {
            return when (last?.symbol) {
                Symbol.ROUTES -> {
                    parseTerminal(Symbol.ROUTES)
                    parseTerminal(Symbol.COLON)
                    parseTerminal(Symbol.LSQ_BRACKET)
                    val routesPrime = ROUTES_PRIME()
                    parseTerminal(Symbol.RSQ_BRACKET)

                    routesPrime
                }
                Symbol.BLOCK_END -> {
                    Routes(mutableListOf<Road>())
                }
                else -> panic()
            }
        }

        fun ROUTES_PRIME(): MAIN_PROGRAM_PRIME_PRIME {
            val routes = mutableListOf<Road>()

            while(last?.symbol == Symbol.ROAD) {
                parseTerminal(Symbol.ROAD)
                parseTerminal(Symbol.BLOCK_START)
                val name = NAME()
                val roadShape = ROAD_SHAPES()
                parseTerminal(Symbol.BLOCK_END)
                parseTerminal(Symbol.SEMICOL)

                routes.add(Road(name, roadShape))
            }

            if (routes.size < 1) {
                throw Exception("There should be atleast 1 route if routes is specified")
            }
            return Routes(routes)
        }

        fun ROAD_SHAPES(): MutableList<RoadShape> {
            return when (last?.symbol) {
                Symbol.SHAPE -> {
                    parseTerminal(Symbol.SHAPE)
                    parseTerminal(Symbol.COLON)
                    parseTerminal(Symbol.BLOCK_START)
                    val roadShapes = mutableListOf<RoadShape>()
                    while(last?.symbol == Symbol.LINE || last?.symbol == Symbol.BEND) {
                        val roadShape = ROAD_SHAPE()
                        roadShapes.add(roadShape)
                    }
                    parseTerminal(Symbol.BLOCK_END)

                    roadShapes
                }
                else -> panic()
            }
        }

    fun ROAD_SHAPE(): RoadShape {
        return when (last?.symbol) {
            Symbol.LINE -> {
                parseTerminal(Symbol.LINE)
                parseTerminal(Symbol.LSQ_BRACKET)
                val leftCoord = COORD()
                parseTerminal(Symbol.COMMA)
                val rightCoord = COORD()
                parseTerminal(Symbol.RSQ_BRACKET)
                parseTerminal(Symbol.SEMICOL)

                RoadShape(Line(leftCoord, rightCoord))
            }
            Symbol.BEND -> {
                parseTerminal(Symbol.BEND)
                parseTerminal(Symbol.LSQ_BRACKET)
                val leftCoord = COORD()
                parseTerminal(Symbol.COMMA)
                val rightCoord = COORD()
                parseTerminal(Symbol.COMMA)
                val expr = parseAdditive()
                parseTerminal(Symbol.RSQ_BRACKET)
                parseTerminal(Symbol.SEMICOL)

                RoadShape(Bend(leftCoord, rightCoord, expr))
            }
            else -> panic()
        }
    }

    /*fun ROADS(): Boolean {
            if (ROAD()) {
                return ROADS_PRIME()
            }
            return false
        }

        fun ROADS_PRIME(): Boolean {
            val lines = mutableListOf<Line>()

            while(last?.symbol == Symbol.LINE) {
                parseTerminal(Symbol.LINE)
                parseTerminal(Symbol.LSQ_BRACKET)
                val leftCoord = COORD()
                parseTerminal(Symbol.COMMA)
                val rightCoord = COORD()
                parseTerminal(Symbol.RSQ_BRACKET)
                parseTerminal(Symbol.SEMICOL)

                lines.add(Line(leftCoord, rightCoord))
            }

            if (lines.size < 3) {
                throw ParseException("There should be atleast 3 lines")
            }
            return Lines(lines)
        }

        fun ROAD(): Boolean {
            if (last?.symbol == Symbol.ROAD) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.VARIABLE) {
                    last = scanner.getToken()
                    if (last?.symbol == Symbol.BLOCK_START) {
                        last = scanner.getToken()
                        if (NAME()) {
                            if (ROAD_SHAPES()) {
                                if (last?.symbol == Symbol.BLOCK_END) {
                                    last = scanner.getToken()
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return false
        }

        fun ROAD_SHAPES(): Boolean {
            if (last?.symbol == Symbol.SHAPE) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.COLON) {
                    last = scanner.getToken()
                    if (last?.symbol == Symbol.BLOCK_START) {
                        last = scanner.getToken()
                        if (ROAD_SHAPES_PRIME()) {
                            if (ROAD_SHAPES()) {
                                if (last?.symbol == Symbol.BLOCK_END) {
                                    last = scanner.getToken()
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return true
        }

        fun ROAD_SHAPES_PRIME(): Boolean {
            if (last?.symbol == Symbol.HASH) {
                last = scanner.getToken()
                if (ROAD_SHAPE()) {
                    return ROAD_SHAPES_PRIME()
                }
            }
            return true
        }


        fun BEND(): Boolean {
            if (last?.symbol == Symbol.BEND) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.LSQ_BRACKET) {
                    last = scanner.getToken()
                    if (COORD()) {
                        if (last?.symbol == Symbol.COMMA) {
                            last = scanner.getToken()
                            if (COORD()) {
                                if (last?.symbol == Symbol.COMMA) {
                                    last = scanner.getToken()
                                    if (EXPRESSION()) {
                                        if (last?.symbol == Symbol.RSQ_BRACKET) {
                                            last = scanner.getToken()
                                            return true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false
        }

        fun FOR(): Boolean {
            if (last?.symbol == Symbol.FOREACH) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.VARIABLE) {
                    last = scanner.getToken()
                    if (last?.symbol == Symbol.IN) {
                        last = scanner.getToken()
                        if (VARIABLE()) {
                            if (last?.symbol == Symbol.BLOCK_START) {
                                last = scanner.getToken()
                                if (PROGRAM()) {
                                    if (last?.symbol == Symbol.BLOCK_END) {
                                        last = scanner.getToken()
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false
        }

        fun VARIABLE(): Boolean {
            if (last?.symbol == Symbol.VARIABLE) {
                last = scanner.getToken()
                return VARIABLE_PRIME()
            } else if (RADIUS()) {
                return true
            }
            return false
        }

        fun VARIABLE_PRIME(): Boolean {
            if (last?.symbol == Symbol.DOT) {
                last = scanner.getToken()
                return VARIABLE_PRIME_PRIME()
            }
            return true
        }

        fun VARIABLE_PRIME_PRIME(): Boolean {
            if (last?.symbol == Symbol.VARIABLE) {
                last = scanner.getToken()
                return true
            } else if (last?.symbol == Symbol.ROUTES) {
                last = scanner.getToken()
                return true
            }
            return false
        }

        fun RADIUS(): Boolean {
            if (last?.symbol == Symbol.RADIUS) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.LSQ_BRACKET) {
                    last = scanner.getToken()
                    if (COORD()) {
                        if (last?.symbol == Symbol.COMMA) {
                            last = scanner.getToken()
                            if (EXPRESSION()) {
                                if (last?.symbol == Symbol.RSQ_BRACKET) {
                                    last = scanner.getToken()
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return false
        }

        fun PROGRAM(): Boolean {
            val result = PROGRAM_PRIME_PRIME()
            if (result && last?.symbol == Symbol.SEMICOL) {
                last = scanner.getToken()
                return PROGRAM_PRIME()
            } else {
                return false
            }
        }

        fun PROGRAM_PRIME(): Boolean {
            if (PROGRAM()) {
                return true
            }
            return true
        }

        fun PROGRAM_PRIME_PRIME(): Boolean {
            if (DECLARATION()) {
                return true
            } else if (ASSIGNMENT()) {
                return true
            } else if (PRINT()) {
                return true
            } else if (HIGHLIGHT()) {
                return true
            } else {
                return false
            }
        }

        fun HIGHLIGHT(): Boolean {
            if (last?.symbol == Symbol.HIGHLIGHT) {
                last = scanner.getToken()
                if (last?.symbol == Symbol.LBRACKET) {
                    last = scanner.getToken()
                    if (VARIABLE()) {
                        if (last?.symbol == Symbol.RBRACKET) {
                            last = scanner.getToken()
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun PARSE(): Boolean {
            val result = MAIN_PROGRAM()
            return result && last?.symbol == Symbol.EOF
        } */

    private fun parseAdditive(): Expr {
        return parseAdditive1(parseMultiplicative())
    }

    private fun parseAdditive1(acc: Expr): Expr {
        return when (last?.symbol) {
            Symbol.PLUS -> {
                parseTerminal(Symbol.PLUS)
                /*return*/
                parseAdditive1(Plus(acc, parseMultiplicative()))
            }
            Symbol.MINUS -> {
                parseTerminal(Symbol.MINUS)
                /*return*/
                parseAdditive1(Minus(acc, parseMultiplicative()))
            }
            Symbol.REAL, Symbol.VARIABLE, Symbol.TIMES, Symbol.DIVIDES, Symbol.INTEGERDIVIDES, Symbol.POW, Symbol.RBRACKET, Symbol.EOF, Symbol.SEMICOL, Symbol.COMMA, Symbol.RSQ_BRACKET -> acc
            else -> panic()
        }
    }


    private fun parseMultiplicative(): Expr {
        return parseMultiplicative1(parseExponential())
    }

    private fun parseMultiplicative1(acc: Expr): Expr {
        return when (last?.symbol) {
            Symbol.TIMES -> {
                parseTerminal(Symbol.TIMES)
                /*return*/
                parseMultiplicative1(Times(acc, parseExponential()))
            }
            Symbol.DIVIDES -> {
                parseTerminal(Symbol.DIVIDES)
                /*return*/
                parseMultiplicative1(Divides(acc, parseExponential()))
            }
            Symbol.INTEGERDIVIDES -> {
                parseTerminal(Symbol.INTEGERDIVIDES)
                /*return*/
                parseMultiplicative1(IntegerDivides(acc, parseExponential()))
            }
            Symbol.REAL, Symbol.VARIABLE, Symbol.PLUS, Symbol.MINUS, Symbol.POW, Symbol.RBRACKET, Symbol.EOF, Symbol.COMMA, Symbol.RSQ_BRACKET -> acc
            else -> panic()
        }
    }

    private fun parseExponential(): Expr {
        return parseExponential1(parseUnary())
    }

    private fun parseExponential1(acc: Expr): Expr {
        return when (last?.symbol) {
            Symbol.POW -> {
                parseTerminal(Symbol.POW)
                val right = parseExponential()
                /*return*/
                Pow(acc, right)
            }
            Symbol.REAL, Symbol.VARIABLE, Symbol.TIMES, Symbol.DIVIDES, Symbol.INTEGERDIVIDES, Symbol.PLUS, Symbol.MINUS, Symbol.RBRACKET, Symbol.EOF, Symbol.COMMA, Symbol.RSQ_BRACKET -> acc
            else -> panic()
        }
    }
    private fun parseUnary(): Expr {
        return when (last?.symbol) {
            Symbol.PLUS -> {
                parseTerminal(Symbol.PLUS)
                /*return*/
                UnaryPlus(parsePrimary())
            }
            Symbol.MINUS -> {
                parseTerminal(Symbol.MINUS)
                /*return*/
                UnaryMinus(parsePrimary())
            }
            Symbol.REAL, Symbol.VARIABLE, Symbol.LBRACKET -> parsePrimary()
            else -> panic()
        }
    }

    private fun parsePrimary(): Expr {
        return when (last?.symbol) {
            Symbol.REAL -> Real(parseTerminal(Symbol.REAL).toDouble())
            Symbol.VARIABLE -> Variable(parseTerminal(Symbol.VARIABLE))
            Symbol.LBRACKET -> {
                parseTerminal(Symbol.LBRACKET)
                val exprResult = parseAdditive()
                parseTerminal(Symbol.RBRACKET)
                /*return*/
                exprResult
            }
            else -> panic()
        }
    }

    private fun parseTerminal(symbol: Symbol): String =
        if (last?.symbol == symbol) {
            val lexme = last!!.lexeme
            last = scanner.getToken()
            lexme
        } else {
            panic()
        }
}

fun main(args: Array<String>) {
    val input = File(args[0]).readText()

    val result = Parser(Scanner(ForForeachFFFAutomaton, input.byteInputStream())).MAIN()

    println(result.toGeoJSON())
    /*za izpis tokenov
    printTokens(Scanner(ForForeachFFFAutomaton, File(args[0]).inputStream()), File(args[1]).outputStream())

     za parsanje oz. sintakticni del
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
    */
}
