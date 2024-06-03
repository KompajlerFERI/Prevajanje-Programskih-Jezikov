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

Main_Program ::= Main_Program’’ ; Main_Program’                                       //////////////////////////////////////////////
Main_Program’ ::= Main_Program                                                        //////////////////////////////////////////////
Main_Program’ ::= ''                                                                  //////////////////////////////////////////////
Main_Program’’ ::= Declaration                                                        //////////////////////////////////////////////
Main_Program’’ ::= Assignment                                                         //////////////////////////////////////////////
Main_Program’’ ::= City                                                               //////////////////////////////////////////////
Declaration ::= const Declaration’                                                    //////////////////////////////////////////////
Declaration ::= Declaration’                                                          //////////////////////////////////////////////
Declaration’ ::= coord spr = Coord                                                    //////////////////////////////////////////////
Declaration’ ::= string spr = String                                                  //////////////////////////////////////////////
Declaration’ ::= num spr = Expression                                                 //////////////////////////////////////////////
Declaration’ ::= List spr = ListContent                                               //////////////////////////////////////////////
DeclarationList’ ::= Type                                                             //////////////////////////////////////////////
DeclarationList’ ::= restaurant                                                       //////////////////////////////////////////////
DeclarationList’ ::= city                                                             //////////////////////////////////////////////
Assignment ::= # spr = Assignment’                                                    //////////////////////////////////////////////
Assignment’ ::= coord Coord                                                           //////////////////////////////////////////////
Assignment’ ::= num Expression                                                        //////////////////////////////////////////////
Assignment’ ::= string String                                                         //////////////////////////////////////////////
Assignment’ ::= list ListContent                                                      //////////////////////////////////////////////
List ::= list < DeclarationList’ >                                                    //////////////////////////////////////////////
ListContent ::= [ Inner_List ]                                                        //////////////////////////////////////////////
Inner_List ::= Expression Inner_List’                                                 //////////////////////////////////////////////
Inner_List ::= ''                                                                     //////////////////////////////////////////////
Inner_List’ ::= , Expression Inner_List’                                              //////////////////////////////////////////////
Inner_List’ ::= ''                                                                    //////////////////////////////////////////////
Type ::= num                                                                          //////////////////////////////////////////////
Type ::= string                                                                       //////////////////////////////////////////////
Type ::= coord                                                                        //////////////////////////////////////////////
Expression ::= Additive                                                               //////////////////////////////////////////////
Additive ::= Multiplicative Additive’                                                 //////////////////////////////////////////////
Additive’ ::= + Multiplicative Additive’                                              //////////////////////////////////////////////
Additive’ ::= - Multiplicative Additive’                                              //////////////////////////////////////////////
Additive’ ::= ''                                                                      //////////////////////////////////////////////
Multiplicative ::= Exponential Multiplicative’                                        //////////////////////////////////////////////
Multiplicative’ ::= * Exponential Multiplicative’                                     //////////////////////////////////////////////
Multiplicative’ ::= / Exponential Multiplicative’                                     //////////////////////////////////////////////
Multiplicative’ ::= // Exponential Multiplicative’                                    //////////////////////////////////////////////
Multiplicative’ ::= ''                                                                //////////////////////////////////////////////
Exponential ::= Unary Exponential’                                                    //////////////////////////////////////////////
Exponential’ ::= ^ Unary Exponential’                                                 //////////////////////////////////////////////
Exponential’ ::= ''                                                                   //////////////////////////////////////////////
Unary ::= + Primary                                                                   //////////////////////////////////////////////
Unary ::= - Primary                                                                   //////////////////////////////////////////////
Unary ::= Primary                                                                     //////////////////////////////////////////////
Primary ::= 0                                                                         //////////////////////////////////////////////
Primary ::= spr                                                                       //////////////////////////////////////////////
Primary ::= ( Additive )                                                              //////////////////////////////////////////////
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

spr -> variable
0 -> {0-9}+({.}{0-9}+)?
a -> {A,…,Z,a,…,z,_}{A,…,Z,a,…,z,0,…,9,_}*



// primer:
const num spr = 0 ; 
const num spr = 0 ; 
string spr = a ; 
coord spr = ( 0 , 0 ) ; 
list < string > spr = [ ] ; 

# spr = coord ( 0 , 0 ) ; 

city spr { 
    restaurant spr { 
        name : a 
        shape : { 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
        } 
        marker : point [ ( 0 , 0 ) ] 
        routes : [ 
            road spr { 
                name : a 
                shape : { 
                    # line [ spr , ( 0 , 0 ) ] 
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] 
                    # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
                    # bend [ ( 0 , 0 ) , spr , 0 ] 
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] 
                } 
            } , 
            road spr { 
                name : a 
                shape : { 
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] 
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] 
                    # line [ ( 0 , 0 ) , spr ] 
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , spr ] 
                } 
            } 
        ] 
    } ; 
    
    restaurant spr { 
        name : a 
        shape : { 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
        }
    } ; 
    
    
    restaurant spr { 
        name : 
        a shape : { 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
        } 
        marker : point [ ( 0 , 0 ) ] 
    } ; 

    num spr = 0 ; 
    restaurant spr { 
        name : a 
        shape : { 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( spr , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ] 
        } 
        marker : point [ ( spr , spr ) ] 
    } ; 
    
    list < num > spr = [ ] ; 
    list < coord > spr = [ spr , spr ] ; 
    
    list < restaurant > spr = [ spr , spr ] ; 
    
    foreach spr in spr . routes { 
        print ( spr ) ; 
    } ; 
    
    foreach spr in radius [ ( 0 , 0 ) , 0 ] { 
        highlight ( spr ) ; 
    } ; 
} ;





// single line primer:
const num spr = 0 ; const num spr = 0 ; string spr = a ; coord spr = ( 0 , 0 ) ; list < string > spr = [ ] ; # spr = coord ( 0 , 0 ) ; city spr { restaurant spr { name : a shape : { # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] } marker : point [ ( 0 , 0 ) ] routes : [ road spr { name : a shape : { # line [ spr , ( 0 , 0 ) ] # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # bend [ ( 0 , 0 ) , spr , 0 ] # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] } } , road spr { name : a shape : { # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ] # line [ ( 0 , 0 ) , spr ] # bend [ ( 0 , 0 ) , ( 0 , 0 ) , spr ] } } ] } ; restaurant spr { name : a shape : { # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] } } ; restaurant spr { name : a shape : { # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] } marker : point [ ( 0 , 0 ) ] } ; num spr = 0 ; restaurant spr { name : a shape : { # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( spr , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] # line [ ( 0 , 0 ) , ( 0 , 0 ) ] } marker : point [ ( spr , spr ) ] } ; list < num > spr = [ ] ; list < coord > spr = [ spr , spr ] ; list < restaurant > spr = [ spr , spr ] ; foreach spr in spr . routes { print ( spr ) ; } ; foreach spr in radius [ ( 0 , 0 ) , 0 ] { highlight ( spr ) ; } ; } ;

// spletna stran za preverjanje LL(1) (nadomestiti -> z ::=):
// https://www.cs.princeton.edu/courses/archive/spring20/cos320/LL1/


// spletna stran za preverjanje primerov (nadomestiti ::= z ->):
// https://jsmachines.sourceforge.net/machines/ll1.html