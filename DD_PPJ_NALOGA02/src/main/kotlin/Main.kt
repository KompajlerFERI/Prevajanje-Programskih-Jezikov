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
/*
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
*/

// BNF:
/*
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

class Parser(private val lex: Scanner) {
    private var token = lex.getToken() // tega vzame že, ko se kliče

    fun MAIN_PROGRAM(): Boolean {
        if (MAIN_PROGRAM_PRIME_PRIME()) {
            if (token.symbol == Symbol.SEMICOL) {
                token = lex.getToken()
                return MAIN_PROGRAM_PRIME()
            }
        }
        return false
    }

    fun MAIN_PROGRAM_PRIME(): Boolean {
        if (MAIN_PROGRAM()) {
            return true
        } else {
            return true
        }
    }

    fun MAIN_PROGRAM_PRIME_PRIME(): Boolean {
        if (DECLARATION()) {
            return true
        } else if (ASSIGNMENT()) {
            return true
        } else if (CITY()) {
            return true
        } else {
            return false
        }
    }

    fun DECLARATION(): Boolean {
        if (token.symbol == Symbol.CONST) {
            token = lex.getToken()
            return DECLARATION_PRIME()
        } else {
            return DECLARATION_PRIME()
        }
    }

    fun DECLARATION_PRIME(): Boolean {
        if (token.symbol == Symbol.TYPE_COORDINATE) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.EQUALS) {
                    token = lex.getToken()
                    return COORD()
                }
            }
        } else if (token.symbol == Symbol.TYPE_STRING) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.EQUALS) {
                    token = lex.getToken()
                    return STRING()
                }
            }
        } else if (token.symbol == Symbol.TYPE_NUMBER) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.EQUALS) {
                    token = lex.getToken()
                    return EXPRESSION()
                }
            }
        } else if (LIST()) {
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.EQUALS) {
                    token = lex.getToken()
                    return LIST_CONTENT()
                }
            }
        }
        return false
    }

    fun DECLARATION_LIST_PRIME(): Boolean {
        if (TYPE()) {
            return true
        } else if (token.symbol == Symbol.RESTAURANT) {
            token = lex.getToken()
            return true
        } else if (token.symbol == Symbol.CITY) {
            token = lex.getToken()
            return true
        } else {
            return false
        }
    }

    fun ASSIGNMENT(): Boolean {
        if (token.symbol == Symbol.HASH) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.EQUALS) {
                    token = lex.getToken()
                    return ASSIGNMENT_PRIME()
                }
            }
        }
        return false
    }

    fun ASSIGNMENT_PRIME(): Boolean {
        if (token.symbol == Symbol.TYPE_COORDINATE) {
            token = lex.getToken()
            return COORD()
        } else if (token.symbol == Symbol.TYPE_NUMBER) {
            token = lex.getToken()
            return EXPRESSION()
        } else if (token.symbol == Symbol.TYPE_STRING) {
            token = lex.getToken()
            return STRING()
        } else if (LIST()) {
            return LIST_CONTENT()
        }
        return false
    }

    fun LIST(): Boolean {
        if (token.symbol == Symbol.TYPE_LIST) {
            token = lex.getToken()
            if (token.symbol == Symbol.LTHAN) {
                token = lex.getToken()
                if (DECLARATION_LIST_PRIME()) {
                    if (token.symbol == Symbol.MTHAN) {
                        token = lex.getToken()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun LIST_CONTENT(): Boolean {
        if (token.symbol == Symbol.LSQ_BRACKET) {
            token = lex.getToken()
            if (INNER_LIST()) {
                if (token.symbol == Symbol.RSQ_BRACKET) {
                    token = lex.getToken()
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
        if (token.symbol == Symbol.COMMA) {
            token = lex.getToken()
            if (EXPRESSION()) {
                return INNER_LIST_PRIME()
            }
        }
        return true
    }

    fun TYPE(): Boolean {
        return when (token.symbol) {
            Symbol.TYPE_NUMBER -> {
                token = lex.getToken()
                true
            }
            Symbol.TYPE_STRING -> {
                token = lex.getToken()
                true
            }
            Symbol.TYPE_COORDINATE -> {
                token = lex.getToken()
                true
            }
            else -> false
        }
    }

    fun EXPRESSION(): Boolean {
        return ADDITIVE()
    }

    fun ADDITIVE(): Boolean {
        return MULTIPLICATIVE() && ADDITIVE_PRIME()
    }

    fun ADDITIVE_PRIME(): Boolean {
        if (token.symbol == Symbol.PLUS || token.symbol == Symbol.MINUS) {
            token = lex.getToken()
            return MULTIPLICATIVE() && ADDITIVE_PRIME()
        }
        return true
    }

    fun MULTIPLICATIVE(): Boolean {
        return EXPONENTIAL() && MULTIPLICATIVE_PRIME()
    }

    fun MULTIPLICATIVE_PRIME(): Boolean {
        if (token.symbol == Symbol.TIMES || token.symbol == Symbol.DIVIDES || token.symbol == Symbol.INTEGERDIVIDES) {
            token = lex.getToken()
            return EXPONENTIAL() && MULTIPLICATIVE_PRIME()
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
                val result = EXPRESSION()
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

    fun CITY(): Boolean {
        if (token.symbol == Symbol.CITY) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.BLOCK_START) {
                    token = lex.getToken()
                    if (CITY_PRIME()) {
                        if (token.symbol == Symbol.BLOCK_END) {
                            token = lex.getToken()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun CITY_PRIME(): Boolean {
        if (CITY_PRIME_PRIME_PRIME()) {
            if (token.symbol == Symbol.SEMICOL) {
                token = lex.getToken()
                return CITY_PRIME_PRIME()
            }
        }
        return false
    }

    fun CITY_PRIME_PRIME(): Boolean {
        if (CITY_PRIME()) {
            return true
        } else {
            return true
        }
    }

    fun CITY_PRIME_PRIME_PRIME(): Boolean {
        if (CITY_CONSTRUCTS()) {
            return true
        } else if (DECLARATION()) {
            return true
        } else if (ASSIGNMENT()) {
            return true
        } else if (PRINT()) {
            return true
        } else if (FOR()) {
            return true
        } else {
            return false
        }
    }

    fun PRINT(): Boolean {
        if (token.symbol == Symbol.PRINT) {
            token = lex.getToken()
            if (token.symbol == Symbol.LBRACKET) {
                token = lex.getToken()
                if (EXPRESSION()) {
                    if (token.symbol == Symbol.RBRACKET) {
                        token = lex.getToken()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun CITY_CONSTRUCTS(): Boolean {
        if (RESTAURANT()) {
            return true
        } else if (ROAD()) {
            return true
        } else {
            return false
        }
    }

    fun RESTAURANT(): Boolean {
        if (token.symbol == Symbol.RESTAURANT) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.BLOCK_START) {
                    token = lex.getToken()
                    if (NAME()) {
                        if (SHAPE()) {
                            if (MARKER()) {
                                if (ROUTES()) {
                                    if (token.symbol == Symbol.BLOCK_END) {
                                        token = lex.getToken()
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

    fun NAME(): Boolean {
        if (token.symbol == Symbol.NAME) {
            token = lex.getToken()
            if (token.symbol == Symbol.COLON) {
                token = lex.getToken()
                if (STRING()) {
                    return true
                }
            }
        }
        return false
    }

    fun STRING(): Boolean {
        return when (token.symbol) {
            Symbol.VARIABLE -> {
                token = lex.getToken()
                true
            }
            Symbol.STRING -> {
                token = lex.getToken()
                true
            }
            else -> false
        }
    }

    fun SHAPE(): Boolean {
        if (token.symbol == Symbol.SHAPE) {
            token = lex.getToken()
            if (token.symbol == Symbol.COLON) {
                token = lex.getToken()
                if (token.symbol == Symbol.BLOCK_START) {
                    token = lex.getToken()
                    if (LINES()) {
                        if (token.symbol == Symbol.BLOCK_END) {
                            token = lex.getToken()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun LINES(): Boolean {
        if (token.symbol == Symbol.HASH) {
            token = lex.getToken()
            if (LINE()) {
                if (token.symbol == Symbol.HASH) {
                    token = lex.getToken()
                    if (LINE()) {
                        if (token.symbol == Symbol.HASH) {
                            token = lex.getToken()
                            if (LINE()) {
                                return LINE_PRIME()
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    fun LINE_PRIME(): Boolean {
        if (token.symbol == Symbol.HASH) {
            token = lex.getToken()
            if (LINE()) {
                return LINE_PRIME()
            }
        }
        return true
    }

    fun LINE(): Boolean {
        if (token.symbol == Symbol.LINE) {
            token = lex.getToken()
            if (token.symbol == Symbol.LSQ_BRACKET) {
                token = lex.getToken()
                if (COORD()) {
                    if (token.symbol == Symbol.COMMA) {
                        token = lex.getToken()
                        if (COORD()) {
                            if (token.symbol == Symbol.RSQ_BRACKET) {
                                token = lex.getToken()
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    fun COORD(): Boolean {
        if (token.symbol == Symbol.VARIABLE) {
            token = lex.getToken()
            return true
        } else if (token.symbol == Symbol.LBRACKET) {
            token = lex.getToken()
            if (EXPRESSION()) {
                if (token.symbol == Symbol.COMMA) {
                    token = lex.getToken()
                    if (EXPRESSION()) {
                        if (token.symbol == Symbol.RBRACKET) {
                            token = lex.getToken()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun MARKER(): Boolean {
        if (MARKER_PRIME()) {
            return true
        }
        return true
    }

    fun MARKER_PRIME(): Boolean {
        if (token.symbol == Symbol.MARKER) {
            token = lex.getToken()
            if (token.symbol == Symbol.COLON) {
                token = lex.getToken()
                if (POINT()) {
                    return true
                }
            }
        }
        return false
    }

    fun POINT(): Boolean {
        if (token.symbol == Symbol.POINT) {
            token = lex.getToken()
            if (token.symbol == Symbol.LSQ_BRACKET) {
                token = lex.getToken()
                if (COORD()) {
                    if (token.symbol == Symbol.RSQ_BRACKET) {
                        token = lex.getToken()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun ROUTES(): Boolean {
        if (ROUTES_PRIME()) {
            return true
        }
        return true
    }

    fun ROUTES_PRIME(): Boolean {
        if (token.symbol == Symbol.ROUTES) {
            token = lex.getToken()
            if (token.symbol == Symbol.COLON) {
                token = lex.getToken()
                if (token.symbol == Symbol.LSQ_BRACKET) {
                    token = lex.getToken()
                    if (ROADS()) {
                        if (token.symbol == Symbol.RSQ_BRACKET) {
                            token = lex.getToken()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun ROADS(): Boolean {
        if (ROAD()) {
            return ROADS_PRIME()
        }
        return false
    }

    fun ROADS_PRIME(): Boolean {
        if (token.symbol == Symbol.COMMA) {
            token = lex.getToken()
            if (ROAD()) {
                return ROADS_PRIME()
            }
        }
        return true
    }

    fun ROAD(): Boolean {
        if (token.symbol == Symbol.ROAD) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.BLOCK_START) {
                    token = lex.getToken()
                    if (NAME()) {
                        if (ROAD_SHAPES()) {
                            if (token.symbol == Symbol.BLOCK_END) {
                                token = lex.getToken()
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
        if (token.symbol == Symbol.SHAPE) {
            token = lex.getToken()
            if (token.symbol == Symbol.COLON) {
                token = lex.getToken()
                if (token.symbol == Symbol.BLOCK_START) {
                    token = lex.getToken()
                    if (ROAD_SHAPES_PRIME()) {
                        if (ROAD_SHAPES()) {
                            if (token.symbol == Symbol.BLOCK_END) {
                                token = lex.getToken()
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
        if (token.symbol == Symbol.HASH) {
            token = lex.getToken()
            if (ROAD_SHAPE()) {
                return ROAD_SHAPES_PRIME()
            }
        }
        return true
    }

    fun ROAD_SHAPE(): Boolean {
        if (LINE()) {
            return true
        } else if (BEND()) {
            return true
        }
        return false
    }

    fun BEND(): Boolean {
        if (token.symbol == Symbol.BEND) {
            token = lex.getToken()
            if (token.symbol == Symbol.LSQ_BRACKET) {
                token = lex.getToken()
                if (COORD()) {
                    if (token.symbol == Symbol.COMMA) {
                        token = lex.getToken()
                        if (COORD()) {
                            if (token.symbol == Symbol.COMMA) {
                                token = lex.getToken()
                                if (EXPRESSION()) {
                                    if (token.symbol == Symbol.RSQ_BRACKET) {
                                        token = lex.getToken()
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
        if (token.symbol == Symbol.FOREACH) {
            token = lex.getToken()
            if (token.symbol == Symbol.VARIABLE) {
                token = lex.getToken()
                if (token.symbol == Symbol.IN) {
                    token = lex.getToken()
                    if (VARIABLE()) {
                        if (token.symbol == Symbol.BLOCK_START) {
                            token = lex.getToken()
                            if (PROGRAM()) {
                                if (token.symbol == Symbol.BLOCK_END) {
                                    token = lex.getToken()
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
        if (token.symbol == Symbol.VARIABLE) {
            token = lex.getToken()
            return VARIABLE_PRIME()
        } else if (RADIUS()) {
            return true
        }
        return false
    }

    fun VARIABLE_PRIME(): Boolean {
        if (token.symbol == Symbol.DOT) {
            token = lex.getToken()
            return VARIABLE_PRIME_PRIME()
        }
        return true
    }

    fun VARIABLE_PRIME_PRIME(): Boolean {
        if (token.symbol == Symbol.VARIABLE) {
            token = lex.getToken()
            return true
        } else if (token.symbol == Symbol.ROUTES) {
            token = lex.getToken()
            return true
        }
        return false
    }

    fun RADIUS(): Boolean {
        if (token.symbol == Symbol.RADIUS) {
            token = lex.getToken()
            if (token.symbol == Symbol.LSQ_BRACKET) {
                token = lex.getToken()
                if (COORD()) {
                    if (token.symbol == Symbol.COMMA) {
                        token = lex.getToken()
                        if (EXPRESSION()) {
                            if (token.symbol == Symbol.RSQ_BRACKET) {
                                token = lex.getToken()
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
        if (result && token.symbol == Symbol.SEMICOL) {
            token = lex.getToken()
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
        if (token.symbol == Symbol.HIGHLIGHT) {
            token = lex.getToken()
            if (token.symbol == Symbol.LBRACKET) {
                token = lex.getToken()
                if (VARIABLE()) {
                    if (token.symbol == Symbol.RBRACKET) {
                        token = lex.getToken()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun PARSE(): Boolean {
        val result = MAIN_PROGRAM()
        return result && token.symbol == Symbol.EOF
    }
}

fun main(args: Array<String>) {
    // za izpis tokenov
    //printTokens(Scanner(ForForeachFFFAutomaton, File(args[0]).inputStream()), File(args[1]).outputStream())

    // za parsanje oz. sintakticni del
    ///*
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
    //*/
}
