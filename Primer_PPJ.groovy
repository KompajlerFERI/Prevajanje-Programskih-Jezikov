const num spremenljivka = 14
const num spremenljivka = 14.421
string spremenljivka2 = "restavracija"
coord coordinate = (46.245267, 15.234526) //(num, num)

city ime {
    restaurant r1 {
        name: "name"
        shape: { //preverja se, da je oblika smiselna (da se ne križa...)
            line[(0, 0), (0, 2)] //line[coord, coord]
            line[(0, 2), (2, 2)]
            line[(2, 2), (2, 0)]
            line[(2, 0), (0, 0)]
        }
        marker: point[(1, 2)]//Če ni podan se izručna na sredini polygona point[coord]
        routes: List<road> = [road1, road2, ...] //Če ni podan, je prazen seznam
    } 
    restaurant r2 {
        name: "name"
        shape: {
            line[(0, 0), (0, 2)]
            line[(0, 2), (2, 2)]
            line[(2, 2), (0, 0)]
        }
        //tu ni marker zato ga more samo zračunat glede na sredino shape-a
        //tudi routes ni nastavljen, torej prazen list
    } 
    restaurant r3 {
        name: "name"
        shape: {
            line[(0, 0), (0, 2)]
            line[(0, 2), (2, 2)]
            line[(2, 2), (2, 0)]
            line[(2, 0), (2, 4)]
            line[(2, 4), (0, 0)]
        }
        marker nameSpremenjivke
    } 

    number p1 = 46.002300

    restaurant r4 {
        name: "Restavracija zlata srna"
        shape: {
            line[(46,928398, 0), (0, 2)]
            line[(p1, 2), (2, 2)]
            line[(2, 2), (2, 0)]
            line[(2, 0), (2 , 4)]
            line[(2, 4), (0 , 0)]
        }
        marker[(x, y)] //središče restavracije (če ni podano se izračuna sredina)
    } 

    road name {
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
    }

    road delivery1 {
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
    }

    List<tip> name = []
    List<coordinate> = [coord1, coord2...]
    List<restaurant> = [r1, r2...]

    foreach route in name.routes {
        print(route.name)
    }

    radius name = ((1, 4), 4) //radius(coord, distance)
    foreach x in radius {
        highlight(x)
    }

    marker name = (8, 3)
}