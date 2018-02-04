
var ONLINE_PLAYERS ="../Lobby/OnlineUsers";
var ONLINE_GAME_ROOMS= "../Lobby/gameRooms"
var refreshRate=2000;




function RefreshActivePlayersList(activeplayers)
{
    //clear all active players list
    $("#activeplayerslist").empty();

    //rebuild
    $.each(activeplayers || [],function(index,username){
        $('<li>'+username+'</li>').appendTo($("#activeplayerslist"));
    });
}


function RefreshGameRooms(GameRooms)
{
    //clear
    $("#GameRoomsTable").empty();

    //rebuild
    $('<tr>'+"<th>Game Name</th>"+"<th>Owner</th>"+"<th>Hands Count</th>"+"<th>Buy size</th>"+"<th>Blinds</th>"+"<th>Num of Players</th>"+"<th>Status</th>"+"<th>Join Game</th>"+'</tr>').appendTo($("#GameRoomsTable"));


    $.each(GameRooms || [], function (index,gameroom) {

        var Id='id'+index;

        $('<tr>' + '<td>' + gameroom.gameTitle + '</td>' + '<td>' + gameroom.owner + '</td>' + '<td>' + gameroom.handsCount + '</td>' +
            '<td>' + gameroom.buySize + '</td>' +
            '<td>' + '<table class="blinds">'
            + '<tr>' +
            "<th>Big Blind</th>\n" +
            "<th>Small Blind</th>\n" +
            "<th>Blind Fixed</th>\n" +
            "<th>blind Addition</th>\n" +
            '</tr>'
            +'<tr>'+
            '<td>'+gameroom.bigBlind+'</td>'+'<td>'+gameroom.smallBlind+'</td>'+'<td>'+gameroom.isBlindFixed+'</td>'+
            '<td>'+gameroom.blindAddition+'</td>'+
            '</tr>'+'</table>'+'</td>'+'<td>'+gameroom.registeredPlayers+'/'+gameroom.maxPlayers+'</td>'+'<td>'+gameroom.gameStatus+'</td>'+'<td>'+'<input type="button" value="Join Game" id="ChangeID"/>'+'<td>'+'</tr>').appendTo($('#GameRoomsTable'));


        $('#ChangeID').attr('id',Id);

        $("#"+Id).onclick(function() {

            window.open("./GameRoomPage.html");
        });








    });





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

    $("#uploadForm").submit(function () {

        var file1 = this[0].files[0];
        var formData = new FormData();
        formData.append("fake-key-1", file1);
        if(file1.name=="")
        {
            return;
        }

        $.ajax({

            method: 'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function (e) {
                console.error("Failed to submit");
                alert("Failed to get result from server " + e);
            },
            success: function (r) {


                if (r.isSucceed == true) {
                    alert("Upload Succeeded!!");

                }
                else {
                    alert(r.msgError);

                }

            }

        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;



    });

    $("#file").change(function () {
        $("#uploadForm").submit();
        $("#file").value="";
    });
});




