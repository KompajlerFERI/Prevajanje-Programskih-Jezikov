V Main vključimo glavni program, saj se Main_Program lahko ponavlja. Znotraj njega vsebuje celoten program.
    Main ::= Main_Program
Main_Program vključuje deklaracije, dodelitve in mesta ter je sestavljen tako, da se lahko ponavlja in da mora obvezno vsebovati vsaj eno deklaracijo, dodelitev ali mesto.
    Main_Program ::= Main_Program’’ ; Main_Program’
Z Main_Program’ lahko ponovimo Main_Program, lahko pa tudi ne.
    Main_Program’ ::= Main_Program
    Main_Program’ ::= ''
Main_Program’’ vsebuje deklaracije, dodelitve in mesta.
    Main_Program’’ ::= Declaration
    Main_Program’’ ::= Assignment
    Main_Program’’ ::= City
Deklaracija je sestavljena iz dekleracije, ki je konstanta in se nesme spreminjati čez program ter navadne deklaracije, ki se lahko spreminja čez program.
    Declaration ::= const Declaration’
    Declaration ::= Declaration’
Declaration’ je sestavljena iz tipa, imena in vrednosti. Definiramo lahko število, niz, koordinata ali list in vsaka od teh ima pripadajočo definicijo, katere lahko pozneje uporabimo v programu s klicanjem imena dekleracije. 
    Declaration’ ::= coord spr = Coord
    Declaration’ ::= string spr = String
    Declaration’ ::= num spr = Expression
    Declaration’ ::= List spr = ListContent
DeclarationList’ je lahko restavracija, mesto, število, niz ali koordinata. Namen je deklaracija tipa lista, da nemore biti več tipov v enem listu.
    DeclarationList’ ::= num
    DeclarationList’ ::= string
    DeclarationList’ ::= coord
    DeclarationList’ ::= restaurant
    DeclarationList’ ::= city
Dodelitev Assignment je sestavljena iz znaka # (brez njega BNF ni LL(1)), imena in vrednosti. Namen je nova dodelitev že dekleriranim imenom.
    Assignment ::= # spr = Assignment’
Assignment’ je sestavljen iz koordinate, števila, niza ali seznama in od vsake pripadajoča definicija. Namen je dodelitev vrednosti že dekleriranim imenom, tip pa more biti specificiran zaradi tega, da je lahko BNF LL(1).
    Assignment’ ::= coord Coord
    Assignment’ ::= num Expression
    Assignment’ ::= string String
    Assignment’ ::= list ListContent
List je sestavljen neterminala list, <, deklaracije tipa in >. Namen je definirati seznam, ki je sestavljen iz enega tipa.
    List ::= list < DeclarationList’ >
ListContent je sestavljen iz [, notranjega seznama in ]. Namen je definirati seznam, ki je sestavljen iz več elementov.
    ListContent ::= [ Inner_List ]
Inner_List je sestavljen iz izraza in notranjega seznama. Namen je definirati seznam, ki je sestavljen iz nič ali več elementov.
    Inner_List ::= Expression Inner_List’
    Inner_List ::= ''
Inner_List’ je sestavljen iz vejice, izraza in notranjega seznama. Namen je definirati seznam, ki je sestavljen iz nič ali več elementov.
    Inner_List’ ::= , Expression Inner_List’
    Inner_List’ ::= ''
Expression je sestavljen iz seštevanja in bo vedno vračal število.
    Expression ::= Additive
Additive je sestavljen iz množenja in seštevanja.
    Additive ::= Multiplicative Additive’
Additive’ je sestavljen iz plusa, množenja in seštevanja. Namen je definirati seštevanje, odštevanje ali ničesar dodatnega.
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
    Program’ ::= Program
    Program’ ::= ''
    Program’’ ::= Declaration
    Program’’ ::= Assignment
    Program’’ ::= Print
    Program’’ ::= Highlight
    Highlight ::= highlight ( spr )

    spr -> [a-zA-Z_]+
    a -> ^"(?s).*"$
    0 -> ^-?\d+(\.\d+)?([eE][-+]?\d+)?$