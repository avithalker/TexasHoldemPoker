var refreshRate=2000;
var is_ready="false";
var is_game_started="false";
var is_hand_started="false"
var interval_id_hand_started;
var interval_id_hand_ended;
var interval_id_game_started;
var interval_id_my_turn;
var bet_value;
var raise_value;
var computer=false;
var players_array=["#player1","#player2","#player3","#player4","#player5","#player6"];
var cards1_images_array=["#card1pl1","#card1pl2","#card1pl3","#card1pl4","#card1pl5","#card1pl6"];
var cards2_images_array=["#card1p21","#card2pl2","#card2pl3","#card2pl4","#card2pl5","#card2pl6"];






function ajaxGetGameDetails()
{
    $.ajax(
        {
            url:"/GameRoom/gameInfo",
            type:'GET',
            success:function(gamedetails){

                updateGameDetails(gamedetails);

            }



        }
    );

}



function updateGameDetails(gamedetails)
{
    $("#buy_value").text(gamedetails.buyValue);
    $("#hands_count").text(gamedetails.handsCount);
    $("#total_played_hands").text(gamedetails.totalPlayedHands);
    $("#big_blind_value").text(gamedetails.bigBlindValue);
    $("#small_blind_value").text(gamedetails.smallBlindValue);
    $("#fixed_value").text(gamedetails.isBlindFixed);
    $("#additions").text(gamedetails.blindAdditions);
    $("#total_tokens").text(gamedetails.totalTokens);

    /*{
       "buyValue": xxx,
       "handsCount": xxx,
       "totalPlayedHands": xxx,
       "bigBlindValue": xxx,
       "smallBlindValue": xxx,
       "isBlindFixed": true/false,
       "blindAdditions": xxx,
       "totalTokens": xxx
   }
   */
}


function  ajaxIsGameStarted()
{
    $.ajax({
        url: "/GameRoom/isGameStarted",
        success: function(r) {
            if(r.result == true)
            {
                clearInterval(interval_id_game_started);
                setGameStarted();
            }

        }
    });

}

function ajaxBuyTokens()
{
    $.ajax({
        url:"/GameRoom/buyTokens",
        type:'POST',
        success: function (res) {

            if(res.isSuccess==false)
            {
                window.alert(res.msgError);
            }
        }



    });
}

function setUiGameStartedMode()
{
    $("#ready_button").disabled=false;
    $("#buy_tokens_button").disabled=false;
    $("#exit_game_button").disabled=false;
    $("#fold_button").disabled=true;
    $("#call_button").disabled=true;
    $("#check_button").disabled=true;
    $("#bet_button").disabled=true;
    $("#raise_button").disables=true;
    $("#raise_value_button").disabled=true;
    $("#bet_value_button").disabled=true;


}


function ajaxIsGameEnded()
{
    //todo:servlet for is game ended...
}


function setGameStarted()
{
    setUiGameStartedMode();
    setInterval(ajaxGetPlayersInfo,refreshRate); //refresh players' data in the table


    //question we need to ask during "game has started" mode:
    setInterval(ajaxIsHandStarted,refreshRate);
    setInterval(ajaxIsGameEnded,refreshRate);
}


function ajaxIsHandEnded()
{
    $.ajax({

        url:"/GameRoom/isHandRoundEnded",
        type:'GET',
        success: function(res){

            if(res.Result==true)
            {
              clearInterval(interval_id_hand_ended);
              setHandEndedMode();
            }
        }

    });
}

function ajaxGetWinners()
{
    $.ajax({

        url:"/GameRoom/winners",
        type:'GET',
        success: function(winners){

            //display winners


        }

});

}


function setHandEndedMode()
{
    //GetWinnersrequest
    ajaxGetWinners();

    //restart new hand
    interval_id_hand_started=setInterval(ajaxIsHandStarted,refreshRate);

}

function setUiHandStarted()
{
    $("#ready_button").disabled=true;
    $("#buy_tokens_button").disabled=true;
    $("#exit_game_button").disabled=true;
    $("#fold_button").disabled=false;
    $("#call_button").disabled=false;
    $("#check_button").disabled=false;
    $("#bet_button").disabled=false;
    $("#raise_button").disables=false;
    $("#raise_value_button").disabled=false;
    $("#bet_value_button").disabled=false;

}


function setPlayersGameStatus(players)
{


    /*<div id="player2" class="players">
            <table class="tableplayer">
                <tr>
                    <th>Name:</th>
                    <td></td>
                </tr>
                </tr>
                <th>Title</th>
                <td></td>
                </tr>
                <tr>
                    <th>Tokens:</th>
                    <td></td>
                </tr>
                <tr>
                    <th>Current Bet:</th>
                    <td></td>
                </tr>
                <tr>
                    <th>Last Action:</th>
                    <td></td>
                </tr>
            </table>
            <div class="card1">

            </div>
            <div class="card2">

            </div>
        </div>*/
    var index=0;
    $.each(players || [], function (index,player_detail) {

        //  <img id="table" src="../Pages/table.png" />

        var card1_src= "./cards/"+player_detail.playerCards[0]+".png";
        var card2_src= "./cards/"+player_detail.playerCards[1]+".png";


        $('<table>'+'<tr>'+'<th>'+'Name:'+'</th>'+'<td>'+player_detail.playerName+'</td>'+'</tr>'

                +'<tr>'+'<th>'+'Title:'+'</th>'+'<td>'+player_detail.playerTitle+'</td>'+'</tr>'

                    +'<tr>'+'<th>'+'Tokens:'+'</th>'+'<td>'+player_detail.tokens+'</td>'+'</tr>'

                    +'<tr>'+'<th>'+'Current Bet'+'</th>'+'<td>'+player_detail.currentBet+'</td>'+'</tr>'

                    +'<tr>'+'<th>'+'Last Action'+'</th>'+'<td>'+player_detail.lastAction+'</td>'+'</tr>'+'</table>'+'<img class="card1"/>'+'<img  class="card2" />').appendTo($(players_array[index]));


                $(player_detail[index]+ img.card1).attr('src',card1_src);
                $(player_detail[index]+ img.card2).attr('src',card2_src);





    });
}

function ajaxGetPlayersGameStatus()
{
    $.ajax({

        url:"/GameRoom/PlayersGameStatus",
        type:'GET',
        success: function(players){

            setPlayersGameStatus(players);


        }



    });
}




function setHandStarted()
{

    setUiHandStarted(); //update data in UI to hand started mode
    setInterval(ajaxIsHandEnded,refreshRate);
    setInterval(ajaxGetPlayersGameStatus,refreshRate) //refresh players data in the board
    setInterval(ajaxIsMyTurn,refreshRate);
    setInterval(ajaxGetPlayersTableInfo,refreshRate);
    //get table details

}

function ajaxIsHandStarted()
{
    $.ajax({
        url: "/GameRoom/isHandStarted",
        success: function(r) {
            if(r.result == true)
            {
                is_hand_started="true";
            }
        }
    });

    if(is_hand_started=="true")
    {
        clearInterval(interval_id_hand_started);
        setHandStarted(); //change display to hand  has started mode.
    }
}

function ajaxReadyToStart()
{
    if(is_ready=="true")
    {
        is_ready="false";
        $("#ready_button").attr('value','Not Ready');
    }
    else
    {
        is_ready="true";
        $("#ready_button").attr('value',"I'm Ready");

    }
    $.ajax(
        {
            url:"/GameRoom/readyToStart",
            type:'POST',
            data: "isReady="+is_ready,
            dataType:"json",
            success:function(r){

                if(r.isSucceed==false)
                {
                    window.alert(r.msgError);
                }
            }

        }
    );

}


function setUiMyTurn()
{

}


function ajaxIsMyTurn()
{
    $.ajax({

        url:"/GameRoom/isMyTurn",
        type:'GET',
        success: function(r){

            if(r.result==true)
            {
               clearInterval(interval_id_my_turn);
               setUiMyTurn(); //set the ui to my_turn mode;
            }

        }



    });
}


function ajaxFoldAction()
{
    $.ajax({

        url:"/GameRoom/makeAction",
        type:'POST',
        data:"action=4value=0",
        success: function(r){
            if(r.isSuccess==false)
            {
                window.alert(r.msgError);
            }
        }

    });
}

function ajaxCheckAction()
{
    $.ajax({
        url:"/GameRoom/makeAction",
        type:'POST',
        data:"action=2value=0",
        success: function(r){
            if(r.isSuccess==false)
            {
                window.alert(r.msgError);
            }
        }

    });
}


function ajaxBetAction()
{
    bet_value=$("#bet_value").textContent;

    $.ajax({

        url:"/GameRoom/makeAction",
        type:'POST',
        data:"action=1value="+bet_value,
        success: function(r){
            if(r.isSuccess==false)
            {
                window.alert(r.msgError);
            }
        }

    });
}


function ajaxRaiseAction()
{
    raise_value=$("#raise_value_button").textContent;

    $.ajax({

        url:"/GameRoom/makeAction",
        type:'POST',
        data:"action=3value="+raise_value,
        success: function(r){
            if(r.isSuccess==false)
            {
                window.alert(r.msgError);
            }
        }

    });
}


function ajaxCallAction()
{
    $.ajax({

        url:"/GameRoom/makeAction",
        type:'POST',
        data:"action=5value=0",
        success: function(r){
            if(r.isSuccess==false)
            {
                window.alert(r.msgError);
            }
        }

    });
}

function  setPlayersTable(playersdata){

    $("#PlayersDetailsTable").empty();

    $('<tr>'+"<th>Name</th>"+"<th>Type</th>"+"<th>Tokens</th>"+"<th>Total Buys</th>"+"<th>Total Winnings</th>"+"<th>Total Hands Played</th>"+'</tr>').appendTo($("#PlayersDetailsTable"));

    $.each(playersdata || [], function (index,player_detail) {


        $('<tr>' + '<td>' + player_detail.playerName + '</td>' + '<td>' +player_detail.playerType+ '</td>' + '<td>' +player_detail.tokens+'</td>' + '<td>' +player_detail.totalBuys+'</td>'+ '<td>' +player_detail.totalWins+'</td>'+ '<td>' +player_detail.totalHandsPlayed+'</td>'
           +'</tr>').appendTo($("#PlayersDetailsTable"));


    });
}

function ajaxGetPlayersInfo()
{
    $.ajax({

        url:"/GameRoom/PlayersInfo",
        type:'GET',
        success: function(res){
            setPlayersTable(res);
        }


    })
}



function  initializeButtons(){

    $("#ready_button").disabled=true;
    $("#buy_tokens_buttons").disabled=true;
    $("#fold_button").disabled=true;
    $("#call_button").disabled=true;
    $("#bet_button").disabled=true;
    $("#check_button").disabled=true;
    $("#raise_button").disabled=true;
    $("#exit_game_button").disabled=false;

}



function ajaxPlayerType()
{
    $.ajax({

        url: "/GameRoom/isComputerType",
        type: 'GET',
        success: function(res){

            computer=res.Result;
        }

    });

}

$(function(){

    initializeButtons();
    ajaxPlayerType();
    ajaxGetGameDetails();
    //get player's type.
    //override click action buttons
    $("#ready_button").click(function(){ajaxReadyToStart();});
    $("#buy_tokens_button").click(function(){ajaxBuyTokens();});
    $("#fold_button").click(function(){ajaxFoldAction();});
    $("#call_button").click(function(){ajaxCallAction();});
    $("#bet_button").click(function(){ajaxBetAction();});
    $("#check_button").click(function(){ajaxCheckAction();});
    $("#raise_button").click(function(){ajaxRaiseAction();});
    //$("#exit_game_button").click(function(){});
    interval_id_game_started=setInterval(ajaxIsGameStarted,refreshRate);



});