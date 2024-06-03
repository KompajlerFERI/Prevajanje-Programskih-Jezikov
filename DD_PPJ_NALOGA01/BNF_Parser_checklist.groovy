Main_Program ::= Main_Program’’ semicol Main_Program’                                               //////////////////////////////////////////
Main_Program’ ::= Main_Program semicol Main_Program’                                                //////////////////////////////////////////
Main_Program’ ::= ''                                                                                //////////////////////////////////////////
Main_Program’’ ::= Declaration                                                                      //////////////////////////////////////////
Main_Program’’ ::= Assignment                                                                       //////////////////////////////////////////
Main_Program’’ ::= City                                                                             //////////////////////////////////////////
Main_Program’’ ::= For                                                                              //////////////////////////////////////////
Declaration ::= const Declaration’                                                                  //////////////////////////////////////////
Declaration ::= Declaration’                                                                        //////////////////////////////////////////
Declaration’ ::= Type variable equals Expression                                                    //nsnZih//////////////////////////////////
Declaration’ ::= List variable equals ListContent                                                   //nsnZih//////////////////////////////////
Assignment ::= hash variable equals Expression                                                      //nsnZih//////////////////////////////////
List ::= type_list lthen Type mthan                                                                 //nsnZih//////////////////////////////////
ListContent ::= lsq_bracket Inner_List rsq_bracket                                                  //nsnZih//////////////////////////////////
Inner_List ::= Expression Inner_List’                                                               //nsnZih//////////////////////////////////
Inner_List’ ::= comma Expression Inner_List’                                                        //nsnZih//////////////////////////////////
Inner_List’ ::= ''                                                                                  //nsnZih//////////////////////////////////
Type ::= type_number                                                                                //nsnZih//////////////////////////////////
Type ::= type_string                                                                                //nsnZih//////////////////////////////////
Type ::= type_coordinate                                                                            //nsnZih//////////////////////////////////
Expression ::= Additive                                                                             //////////////////////////////////////////
Additive ::= Multiplicative Additive’                                                               //////////////////////////////////////////
Additive’ ::= plus Multiplicative Additive’                                                         //////////////////////////////////////////
Additive’ ::= minus Multiplicative Additive’                                                        //////////////////////////////////////////
Additive’ ::= ''                                                                                    //////////////////////////////////////////
Multiplicative ::= Exponential Multiplicative’                                                      //////////////////////////////////////////
Multiplicative’ ::= times Exponential Multiplicative’                                               //////////////////////////////////////////
Multiplicative’ ::= divide Exponential Multiplicative’                                              //////////////////////////////////////////
Multiplicative’ ::= integer-divide Exponential Multiplicative’                                      //////////////////////////////////////////
Multiplicative’ ::= ''                                                                              //////////////////////////////////////////
Exponential ::= Unary Exponential’                                                                  //////////////////////////////////////////
Exponential’ ::= pow Unary Exponential’                                                             //////////////////////////////////////////
Exponential’ ::= ''                                                                                 //////////////////////////////////////////
Unary ::= plus Primary                                                                              //////////////////////////////////////////
Unary ::= minus Primary                                                                             //////////////////////////////////////////
Unary ::= Primary                                                                                   //////////////////////////////////////////
Primary ::= real                                                                                    //////////////////////////////////////////
Primary ::= variable                                                                                //////////////////////////////////////////
Primary ::= lbracket Additive rbracket                                                              //////////////////////////////////////////
City ::= city variable block_start City’ block_end semicol                                          //nsnZih//////////////////////////////////
City’ ::= City’’’ semicol City’’                                                                    //nsnZih//////////////////////////////////
City’’ ::= City’ semicol City’’                                                                     //nsnZih//////////////////////////////////
City’’ ::= ''                                                                                       //nsnZih//////////////////////////////////
City’’’ ::= City_Constructs                                                                         //nsnZih//////////////////////////////////
City’’’ ::= Declaration                                                                             //nsnZih//////////////////////////////////
City’’’ ::= Assignment                                                                              //nsnZih//////////////////////////////////
City’’’ ::= Print                                                                                   //nsnZih//////////////////////////////////
Print ::= print lbracket Expression rbracket                                                        //nsnZih//////////////////////////////////
City_Constructs ::= Restaurant                                                                      //nsnZih//////////////////////////////////
City_Constructs ::= Road                                                                            //nsnZih//////////////////////////////////
Restaurant ::= restaurant variable block_start Name Shape Marker Routes block_end                   //nsnZih//////////////////////////////////
Name ::= name colon String                                                                          //nsnZih//////////////////////////////////
String ::= variable                                                                                 //nsnZih//////////////////////////////////
String ::= string                                                                                   //nsnZih//////////////////////////////////
Shape ::= shape colon block_start Lines block_end                                                   //nsnZih//////////////////////////////////
Lines ::= hash Line hash Line hash Line Line’                                                       //nsnZih//////////////////////////////////
Line’ ::= hash Line Line’                                                                           //nsnZih//////////////////////////////////
Line’ ::= ''                                                                                        //nsnZih//////////////////////////////////
Line ::= line lsq_bracket Coord comma Coord rsq_bracket                                             //nsnZih//////////////////////////////////
Coord ::= variable                                                                                  //nsnZih//////////////////////////////////
Coord ::= lbracket Expression comma Expression rbracket                                             //nsnZih//////////////////////////////////
Marker ::= Marker’                                                                                  //nsnZih//////////////////////////////////
Marker ::= ''                                                                                       //nsnZih//////////////////////////////////
Marker’ ::= marker colon Point                                                                      //nsnZih//////////////////////////////////
Point ::= point lsq_bracket Coord rsq_bracket                                                       //nsnZih//////////////////////////////////
Routes ::= Routes’                                                                                  //nsnZih//////////////////////////////////
Routes ::= ''                                                                                       //nsnZih//////////////////////////////////
Routes’ ::= routes colon lsq_bracket Roads rsq_bracket                                              //nsnZih//////////////////////////////////
Roads ::= Road Road’                                                                                //nsnZih//////////////////////////////////
Road’ ::= comma Road Roads’                                                                         //nsnZih//////////////////////////////////
Road’ ::= ''                                                                                        //nsnZih//////////////////////////////////
Road ::= road variable block_start Name Road_Shapes block_end                                       //nsnZih//////////////////////////////////
Road_Shapes ::= shape colon block_start  Road_Shapes’ Road_Shapes block_end                         //nsnZih//////////////////////////////////
Road_Shapes’ ::= Road_Shape semicol Road_Shapes’                                                    //nsnZih//////////////////////////////////
Road_Shapes’ ::= ''                                                                                 //nsnZih//////////////////////////////////
Road_Shape ::= Line                                                                                 //nsnZih//////////////////////////////////
Road_Shape ::= Bend                                                                                 //nsnZih//////////////////////////////////
Bend ::= hash bend lsq_bracket Coord comma Coord comma Expression rsq_bracket                       //nsnZih//////////////////////////////////
For ::= foreach variable in Variable block_start Program block_end                                  //nsnZih//////////////////////////////////
Variable ::= variable                                                                               //nsnZih//////////////////////////////////
Variable ::= Radius                                                                                 //nsnZih//////////////////////////////////
Radius ::= lsq_bracket Coord comma Expression rsq_bracket                                           //nsnZih//////////////////////////////////
Program ::= Program’’ semicol Program’                                                              //nsnZih//////////////////////////////////
Program’ ::= Program semicol Program’                                                               //nsnZih//////////////////////////////////
Program’ ::= ''                                                                                     //nsnZih//////////////////////////////////
Program’’ ::= Declaration                                                                           //nsnZih//////////////////////////////////
Program’’ ::= Assignment                                                                            //nsnZih//////////////////////////////////
Program’’ ::= Print                                                                                 //nsnZih//////////////////////////////////
Program’’ ::= Highlight                                                                             //nsnZih//////////////////////////////////
Highlight ::= highlight lbracket variable rbracket                                                  //nsnZih//////////////////////////////////



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


Main_Program ::= Main_Program’’ ; Main_Program’
Main_Program’ ::= Main_Program ; Main_Program’
Main_Program’ ::= ''
Main_Program’’ ::= Declaration
Main_Program’’ ::= Assignment
Main_Program’’ ::= City
Main_Program’’ ::= For

Declaration ::= const Declaration’
Declaration ::= Declaration’
Declaration’ ::= Type spr = Expression
Declaration’ ::= List spr = ListContent

Assignment ::= # spr = Expression

List ::= list [ Type ]
ListContent ::= [ Inner_List ]
Inner_List ::= Expression Inner_List’
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

City ::= city spr { City’ } ;
City’ ::= City’’’ ; City’’
City’’ ::= City’ ; City’’
City’’ ::= ''
City’’’ ::= City_Constructs
City’’’ ::= Declaration
City’’’ ::= Assignment
City’’’ ::= Print

Print ::= print ( Expression )

City_Constructs ::= Restaurant
City_Constructs ::= Road

Restaurant ::= restaurant spr { Name Shape Marker Routes }
Name ::= name : String
String ::= spr
String ::= "spr"

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
Road’ ::= , Road Roads’
Road’ ::= ''
Road ::= road spr { Name Road_Shapes }
Road_Shapes ::= shape : { Road_Shapes’ Road_Shapes }
Road_Shapes’ ::= Road_Shape ; Road_Shapes’
Road_Shapes’ ::= ''

Road_Shape ::= Line
Road_Shape ::= Bend
Bend ::= # bend [ Coord , Coord , Expression ]

For ::= foreach spr in Variable { Program }
Variable ::= spr
Variable ::= Radius
Radius ::= [ Coord , Expression ]

Program ::= Program’’ ; Program’
Program’ ::= Program ; Program’
Program’ ::= ''
Program’’ ::= Declaration
Program’’ ::= Assignment
Program’’ ::= Print
Program’’ ::= Highlight

Highlight ::= highlight ( spr )




const num spr = 0;
const num spr = 0;
string spr = "spr";
coord spr = (0, 0);
list<tip> spr = [];

#spr = (0, 0);

city spr {
    restaurant spr {
        name: "spr"
        shape: {
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
        }
        marker: point[(0, 0)]
        routes: [
            road spr {
                name: "spr"
                shape : {
                    #line[spr, (0, 0)]
                    #bend[(0, 0), (0, 0), 0]
                    #line[(0, 0), (0, 0)]
                    #bend[line[(0, 0), spr], 0]
                    #bend[line[(0, 0), (0, 0)], 0]
                }
            }, 
            road spr {
                name: "spr"
                #bend[line[(0, 0), (0, 0)], 0]
                #bend[line[(0, 0), (0, 0)], 0]
                #line[(0, 0), spr]
                #bend[line[(0, 0), (0, 0)], spr]
            }
        ]
    };
    

    restaurant spr {
        name: "spr"
        shape: {
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
        }
    };
    restaurant spr {
        name: "spr"
        shape: {
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
        }
        marker: point[(0, 0)]
    };

    num spr = 0;

    restaurant spr {
        name: "spr"
        shape: {
            #line[(0, 0), (0, 0)]
            #line[(spr, 0), (0, 0)]
            #line[(0, 0), (0, 0)]
            #line[(0, 0), (0 , 0)]
            #line[(0, 0), (0 , 0)]
        }
        marker[(x, y)]
    };

    List<tip> spr = [];
    List<coordinate> = [spr, spr];
    List<restaurant> = [spr, spr];

    foreach spr in name.routes {
        print(spr.name);
    };

    foreach spr in radius[(0, 0), 0] {
        highlight(spr);
    };
};