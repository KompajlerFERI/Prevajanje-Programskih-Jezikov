city ime {
    //lahko se dodamo metapodatke npr nasledne leto število menijev pa ti označi kere vse majo ponudbo
    //lahko damo ceno not pa po ceni filtrira...
    restaurant name {
        name = "name"
        line[(0, 0), (0, 2)]
        line[(0, 2), (2, 2)]
        line[(2, 2), (2, 0)]
        line[(2, 0), (0 , 0)]
        marker[null] //al pa da ga ni spodni primer
    } 
    restaurant name {
        name = "name"
        line[(0, 0), (0, 2)]
        line[(0, 2), (2, 2)]
        line[(2, 2), (0, 0)]
        //tu ni marker zato ga more samo zračunat -> samo se nastavi na null
    } 
    restaurant name {
        name = "name"
        line[(0, 0), (0, 2)]
        line[(0, 2), (2, 2)]
        line[(2, 2), (2, 0)]
        line[(2, 0), (2 , 4)]
        line[(2, 4), (0 , 0)]
        marker[(x, y)] //središče restavracije (če ni podano se izračuna sredina)
    } 

    number p1 = 46.002300

    restaurant r1 {
        name = "Restavracija zlata srna"
        line[(46,928398, 0), (0, 2)]
        line[(p1, 2), (2, 2)]
        line[(2, 2), (2, 0)]
        line[(2, 0), (2 , 4)]
        line[(2, 4), (0 , 0)]
        marker[(x, y)] //središče restavracije (če ni podano se izračuna sredina)
        deliveries = [r1, r2, r3]
    } 

    Road name {
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
    }

    Road delivery1 {

        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
        path(line, curve)
    }

    r1.enterDelivery(delivery1)

    name.deliveries {
        
    }
    let name
    let num = 25
    let str = "restavracija"
    let coord = (46.245267, 15.234526)

    //ali zgorno ali spodno

    number name = 25 //lahko preverimo da so stvari pravilnega tipa, partialEval bi meli checkTypes, enum type nekaj takega pa samo tip posli
    string name = "restavracija"
    coordinate name = (46.245267, 15.234526)

    List<tip> = []
    List<coordinate> = [coord1, coord2...]
    List<restaurant> = [r1, r2...]

    foreach value in name.deliveries {
        print(value)
    }

    radius((1, 4), 4) //radius(coord, distance)
    foreach x in radius {
        highlight(x)
    }

    marker name (8, 3)
}