package cinema

class Seat {
    var state = 'S'
}

class Row(seatNum: Int) {
    var seats = List(seatNum) { Seat() }
}

class RoomInfo {
    var name: String = "Unnamed"
    var rowNum:  Int = 0
    var seatNum: Int = 0
    val priceFront = 10
    val priceBack = 8
}

class Room(roomInfo: RoomInfo) {
    var info: RoomInfo = roomInfo
    var rows = listOf<Row>()

    fun getSeatState(rowNum: Int, seatNum: Int): Char {
        return rows[rowNum].seats[seatNum].state
    }

    fun changeSeatState(rowNum: Int, seatNum: Int, newState: Char) {
        rows[rowNum].seats[seatNum].state = newState
    }
}

class SeatCoords {
    var row = 0
    var seat = 0
}

fun getRoomInfo(): RoomInfo {
    val roomInfo = RoomInfo()
    roomInfo.name = "Cinema"
    println("Enter the number of rows:")
    roomInfo.rowNum = readln().toInt()
    println("Enter the number of seats in each row:")
    roomInfo.seatNum = readln().toInt()
    return roomInfo
}

fun initRoom(roomInfo: RoomInfo): Room {
    val room = Room(roomInfo)
    room.info.name = roomInfo.name
    room.rows = List(room.info.rowNum) { Row(room.info.seatNum) }
    return room
}

fun getUserSeat(room: Room): SeatCoords {
    val userInput = SeatCoords()
    var validCoords: Boolean
    do {
        println("\nEnter a row number:")
        userInput.row = readln().toInt()
        println("Enter a seat number in that row:")
        userInput.seat = readln().toInt()
        if (userInput.row !in 1..room.info.rowNum || userInput.seat !in 1..room.info.seatNum) {
            println("\nWrong input!")
            validCoords = false
        }
        else validCoords = true
    } while (!validCoords)
    userInput.row--
    userInput.seat--
    return userInput
}

fun checkSeatPrice(room: Room, row: Int): Int {
    val price: Int
    if( room.info.rowNum * room.info.seatNum <= 60) price = room.info.priceFront
    else {
        val frontRows: Int = (room.info.rowNum) / 2
        price = if( row + 1 <= frontRows ) room.info.priceFront else room.info.priceBack
    }
    return price
}

fun printRoom(room: Room) {
    println("\n${room.info.name}:")
    print("  ")
    for (i in 1 .. room.rows[0].seats.size)
        print("$i ")
    print("\n")
    for (i in room.rows.indices){
        print("${i + 1} ")
        for (seat in room.rows[i].seats)
            print("${seat.state} ")
        print("\n")
    }
}

fun buySeat(room: Room) {
    var bought = false
    do{
        val inquiry: SeatCoords = getUserSeat(room)
        if (room.getSeatState(inquiry.row, inquiry.seat) == 'B') {
            println("\nThat ticket has already been purchased!")
        } else {
            println("\nTicket price: \$${checkSeatPrice(room, inquiry.row)}")
            room.changeSeatState(inquiry.row, inquiry.seat, 'B')
            bought = true
        }
    } while (!bought)
}

fun statistics(room: Room) {
    var ticketsPurchased = 0
    var incomeCur = 0
    var incomeTotal = 0
    for (rowNum in room.rows.indices){
        for (seatNum in room.rows[rowNum].seats.indices) {
            val price = checkSeatPrice(room, rowNum)
            incomeTotal += price
            if (room.getSeatState(rowNum, seatNum) == 'B') {
                ticketsPurchased++
                incomeCur += price
            }
        }
    }
    val occupancy: Double = (ticketsPurchased.toDouble() / (room.info.rowNum * room.info.seatNum)) * 100
    println("\nNumber of purchased tickets: $ticketsPurchased")
    println("Percentage: ${"%.2f".format(occupancy)}%")
    println("Current income: \$$incomeCur")
    println("Total income: \$$incomeTotal")
}

fun menuLoop(room: Room) {
    var menuChoice: Int
    do {
        do {
            println("\n1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit")
            menuChoice = readln().toInt()
        } while (menuChoice !in 0..3)
        when (menuChoice) {
            1 -> printRoom(room)
            2 -> buySeat(room)
            3 -> statistics(room)
        }
    } while (menuChoice != 0)
}

fun main() {
    val cinema = initRoom(getRoomInfo())
    menuLoop(cinema)
}