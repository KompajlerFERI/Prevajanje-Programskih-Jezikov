Main_Program ::= Main_Program’’ semicol Main_Program’
Main_Program’ ::= Main_Program semicol Main_Program’
Main_Program’ ::= ''
Main_Program’’ ::= Declaration
Main_Program’’ ::= Assignment
Main_Program’’ ::= City
Main_Program’’ ::= For
Declaration ::= const Declaration’
Declaration ::= Declaration’
Declaration’ ::= Type variable equals Expression
Declaration’ ::= List variable equals ListContent
Assignment ::= hash variable equals Expression
List ::= type_list lthen Type mthan
ListContent ::= lsq_bracket Inner_List rsq_bracket
Inner_List ::= Expression Inner_List’
Inner_List’ ::= comma Expression Inner_List’
Inner_List’ ::= ''
Type ::= type_number
Type ::= type_string
Type ::= type_coordinate
Expression ::= Additive
Additive ::= Multiplicative Additive’
Additive’ ::= plus Multiplicative Additive’
Additive’ ::= minus Multiplicative Additive’
Additive’ ::= ''
Multiplicative ::= Exponential Multiplicative’
Multiplicative’ ::= times Exponential Multiplicative’
Multiplicative’ ::= divide Exponential Multiplicative’
Multiplicative’ ::= integer-divide Exponential Multiplicative’
Multiplicative’ ::= ''
Exponential ::= Unary Exponential’
Exponential’ ::= pow Unary Exponential’
Exponential’ ::= ''
Unary ::= plus Primary
Unary ::= minus Primary
Unary ::= Primary
Primary ::= real
Primary ::= variable
Primary ::= lbracket Additive rbracket
City ::= city variable block_start City’ block_end semicol
City’ ::= City’’’ semicol City’’
City’’ ::= City’ semicol City’’
City’’ ::= ''
City’’’ ::= City_Constructs
City’’’ ::= Declaration
City’’’ ::= Assignment
City’’’ ::= Print
Print ::= print lbracket Expression rbracket
City_Constructs ::= Restaurant
City_Constructs ::= Road
Restaurant ::= restaurant variable block_start Name Shape Marker Routes block_end
Name ::= name colon String
String ::= variable
String ::= string
Shape ::= shape colon block_start Lines block_end
Lines ::= hash Line hash Line hash Line Line’
Line’ ::= hash Line Line’
Line’ ::= ''
Line ::= line lsq_bracket Coord comma Coord rsq_bracket
Coord ::= variable
Coord ::= lbracket Expression comma Expression rbracket
Marker ::= Marker’
Marker ::= ''
Marker’ ::= marker colon Point
Point ::= point lsq_bracket Coord rsq_bracket
Routes ::= Routes’
Routes ::= ''
Routes’ ::= routes colon lsq_bracket Roads rsq_bracket
Roads ::= Road Road’
Road’ ::= comma Road Roads’
Road’ ::= ''
Road ::= road variable block_start Name Road_Shapes block_end
Road_Shapes ::= shape colon block_start  Road_Shapes’ Road_Shapes block_end
Road_Shapes’ ::= Road_Shape semicol Road_Shapes’
Road_Shapes’ ::= ''
Road_Shape ::= Line
Road_Shape ::= Bend
Bend ::= hash bend lsq_bracket Coord comma Coord comma Expression rsq_bracket
For ::= foreach variable in Variable block_start Program block_end
Variable ::= variable
Variable ::= Radius
Radius ::= lsq_bracket Coord comma Expression rsq_bracket
Program ::= Program’’ semicol Program’
Program’ ::= Program semicol Program’
Program’ ::= ''
Program’’ ::= Declaration
Program’’ ::= Assignment
Program’’ ::= Print
Program’’ ::= Highlight
Highlight ::= highlight lbracket variable rbracket 

const -> const
variable -> {A,…,Z,a,…,z,_}{A,…,Z,a,…,z,0,…,9,_}*
equals -> =
type_number -> num
type_string -> string
type_coordinate -> coord
type_list -> list
lthan -> <
mthan -> >
city -> city
block_start -> {
block_end -> }
name -> name
colon -> :
string -> “{A,…,Z,a,…,z,0,…,9}*” oz. ASCII
shape -> shape
line -> line
lsq_bracket -> [
comma -> ,
rsq_bracket -> ]
lbracket -> (
rbracket -> )
marker  -> marker
point-> point
routes-> routes
road-> road
bend-> bend
foreach-> foreach
highlight -> highlight
semicol -> ;
hash -> #
real -> {0,…,9}+(.{0,…,9}+)?
plus -> +
minus -> -
times -> *
divide -> /
integer-divide -> //
pow -> ^
print -> print
in -> in
restaurant -> restaurant
