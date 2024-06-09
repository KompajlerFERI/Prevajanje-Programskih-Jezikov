Main ::= Main_Program

Main_Program ::= Main_Program’’  Main_Program’

Main_Program’ ::= Main_Program
Main_Program’ ::= ''

Main_Program’’ ::= Declaration ;
Main_Program’’ ::= Assignment ;
Main_Program’’ ::= City ;

Declaration ::= const Declaration’
Declaration ::= Declaration’

Declaration’ ::= coord spr = Coord
Declaration’ ::= string spr = String
Declaration’ ::= num spr = Expression
Declaration’ ::= List spr = ListContent

DeclarationList’ ::= Type
DeclarationList’ ::= restaurant
DeclarationList’ ::= city
DeclarationList’ ::= num
DeclarationList’ ::= string
DeclarationList’ ::= coord

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

City' ::= Restaurant City''

City'' ::= City'
City'' ::= ''

Restaurant ::= restaurant spr { Name Shape Marker Routes }
Name ::= name : String

String ::= a

Shape ::= shape : { Coords }

Coords ::= Coord ; Coord ; Coord ; Coords’

Coords’ ::= Coord ; Coords’
Coords’ ::= ''

Line ::= line [ Coord , Coord ];

Coord ::= ( Expression , Expression )

Marker ::= marker : Point

Point ::= point [ Coord ]

Routes ::= Routes’
Routes ::= ''

Routes’ ::= routes : [ Roads ]

Roads ::= Road Road’

Road’ ::= , Road Road’
Road’ ::= ''

Road ::= road { Name Road_Shapes }

Road_Shapes ::= shape : { Road_Shape Road_Shapes' }

Road_Shapes' ::= Road_Shape Road_Shapes'
Road_Shapes' ::= ''

Road_Shape ::= Line
Road_Shape ::= Bend

Bend ::= bend [ Coord , Coord , Expression ]



spr -> [a-zA-Z_]+
a -> ^"(?s).*"$
0 -> ^-?\d+(\.\d+)?([eE][-+]?\d+)?$