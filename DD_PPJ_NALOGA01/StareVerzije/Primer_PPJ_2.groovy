const num spremenljivka = 14;
const num spremenljivka = 14.421;
string spremenljivka2 = "restavracija";
coord coordinate = (46.245267, 15.234526);
list<string> name = [];

#coordinate = (16.245267, 45.234526);

city ime {
    restaurant r1 {
        name: "name"
        shape: {
            #line[(0, 0), (0, 2)]
            #line[(0, 2), (2, 2)]
            #line[(2, 2), (2, 0)]
            #line[(2, 0), (0, 0)]
        }
        marker: point[(1, 2)]
        routes: [
            road dostava1 {
                name: "Dostava do nekam, nea vem kam"
                shape : {
                    #line[c1, (4, 2)]
                    #bend[(20, 1), (0, 22), 30]
                    #line[(2, 0), (30, 2)]
                    #bend[line[(0, 5), c4], 0]
                    #bend[line[(20, 24), (0, 2)], 13]
                }
            }, 
            road name {
                name: "Dostava do nekam, nea vem kam2"
                #bend[line[(30, 0), (42, 2)], 0]
                #bend[line[(0, 1), (30, 2)], 30]
                #line[(0, 5), c1]
                #bend[line[(0, 4), (0, 2)], variable1]
            }
        ]
    };
    

    restaurant r2 {
        name: "name"
        shape: {
            #line[(0, 0), (0, 2)]
            #line[(0, 2), (2, 2)]
            #line[(2, 2), (0, 0)]
        }
    };
    restaurant r3 {
        name: "name"
        shape: {
            #line[(0, 0), (0, 2)]
            #line[(0, 2), (2, 2)]
            #line[(2, 2), (2, 0)]
            #line[(2, 0), (2, 4)]
            #line[(2, 4), (0, 0)]
        }
        marker: point[(3, 5)]
    };

    number p1 = 46.002300;

    restaurant r4 {
        name: "Restavracija zlata srna"
        shape: {
            #line[(46,928398, 0), (0, 2)]
            #line[(p1, 2), (2, 2)]
            #line[(2, 2), (2, 0)]
            #line[(2, 0), (2 , 4)]
            #line[(2, 4), (0 , 0)]
        }
        marker[(x, y)]
    };

    List<tip> name = [random];
    List<coordinate> = [coord1, coord2...];
    List<restaurant> = [r1, r2...];

    foreach route in name.routes {
        print(route.name);
    };

    foreach x in radius[(1, 4), 4] {
        highlight(x);
    };
};