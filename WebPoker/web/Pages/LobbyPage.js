
var ONLINE_PLAYERS ="../Lobby/OnlineUsers";
var ONLINE_GAME_ROOMS= "../Lobby/gameRooms"
var refreshRate=2000;




function RefreshActivePlayersList(activeplayers)
{
    //clear all active players list
    $("activeplayerslist").empty();

    //rebuild
    $.each(activeplayers || [],function(username){
        $('<li>'+username+'</li>').appendTo($("#activeplayerslist"));
    });
}


function RefreshGameRooms(GameRooms)
{
    //clear
    $("GameRoomsTable").empty();

    //rebuild
    $.each(GameRooms ||[],function (gameroom){
        $('<tr>'+'<td>'+gameroom.gameTitle+'</td>'+'<td>'+gameroom.Owner+'</td>'+'<td>'+gameroom.handsCount+'</td>'+
            '<td>'+gameroom.buySize+'</td>'+
            '<td>'+'<table class="blinds">'
            +"<tr>\n" +
            "<th>Big Blind</th>\n" +
            "<th>Small Blind</th>\n" +
            "<th>Blind Fixed</th>\n" +
            "<th>blindAddition</th>\n" +
            "</tr>+'<tr>'+'<td>'+gameroom.bigBlind+'</td>'+'<td>'+gameroom.smallBlind+'</td>'+'<td>'+gameroom.isBlindFixed+'</td>'+" +
            "'<td>'+gameroom.blindAddition+'</td>'+'</tr>'+'</table>'+'</td>'+'<td>'+gameroom.registeredPlayers+'</td>'+'<td>'+gameroom.maxPlayers'+'</td>'+'<td>'+gameroom.gameStatus'</td>'+ '</tr>'"
        );
    }
    )


/*   <td><table class="blinds">
<tr>
<th>Big Blind</th>
<th>Small Blind</th>
<th>Blind Fixed</th>
<th>blindAddition</th>
</tr>
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
</tr>
</table></td>*/

}


function ajaxRefreshGameRooms()
{
    $.ajax({
        url: ONLINE_GAME_ROOMS,
        success: function(gamerooms){
            RefreshGameRooms(gamerooms);
        }
    }


)
}

//function tht creates a get request to the online users

function ajaxActivePlayersList() {
    $.ajax({
        url: ONLINE_PLAYERS,
        success: function(users) {
            RefreshActivePlayersList(users);
        }
    });
}



//onload function.

$(function() {

    setInterval(ajaxActivePlayersList, refreshRate);
    setInterval(ajaxRefreshGameRooms,refreshRate);

});




