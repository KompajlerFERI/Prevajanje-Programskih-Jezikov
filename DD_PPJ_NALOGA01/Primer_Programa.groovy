const num spr = 12 - 12;
const num spr = 33.15;
string spr = "HEJ ti!";
coord spr = (0.1 ,123130 );
list <string > spr = [ ojla, 12, 33] ;

#spr=coord( 12 ,33 ) ;

city sprMesto    {
    restaurant spr {
        name: spr
        shape : {
            #line [ ( 0 , 0 ) , ( 0 , 0 ) ]
            # line [ ( 12 , 33 ) , ( 44.5 , 0 ) ]
            # line [ ( 0 , 0 ) , ( 0 , 0 ) ]
            # line [ ( 0 , 4 ) , ( 12 , 0 ) ]
        }
        marker :point [ ( 0 , 0 ) ]
        routes: [
            road spr {
                name : _aaaaaaA_A1
                shape : {
                    # line [ spr , ( 0 , 0 ) ]
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ]
                    # line [ ( 33 -12^ 5 *3 , 0 ) , ( 0 , 0 ) ]
                    # bend [ ( 0 , 0 ) , spr , 0 ]
                    # bend [ ( 0 , 0 ) , ( 0 , 0 ) , 0 ]
                }
            } ,
            road sprawd {
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
            #   line [ ( 0 , 0 ) , ( 0 , 0 ) ]
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
        };

    num spr = 0 - (-33);
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

    list < num > spr = [ kevin, 22, 2222222-2*(2+2)] ;
    list < coord > spr = [ spr , spr ] ;

    list < restaurant > spr = [ spr , spr ] ;

    foreach spr in spr . routes {
        print ( spr ) ;
        print ( 12 + 3 ) ;
        highlight ( spr ) ;
    } ;

    foreach spr in radius [ ( 0 , 12-3) , 0 ] {
        highlight ( spr ) ;
    } ;
} ;
