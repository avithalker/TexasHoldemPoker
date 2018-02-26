var refreshRate=2000;
var is_ready="false";
var is_game_started="false";
var is_hand_started="false";
var interval_id_hand_started;
var interval_id_hand_ended;
var interval_id_game_started;
var interval_id_my_turn;
var bet_value;
var raise_value;
var computer=false;
var players_array=["#player1","#player2","#player3","#player4","#player5","#player6"];
var board_cards=["#card_board1","#card_board2","#card_board3","#card_board4","#card_board5"];






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
    $("#ready_button").prop("disabled", false);
    $("#buy_tokens_button").prop("disabled", false);
    $("#exit_game_button").prop("disabled", false);
    $("#fold_button").prop("disabled", true);
    $("#call_button").prop("disabled", true);
    $("#check_button").prop("disabled", true);
    $("#bet_button").prop("disabled", true);
    $("#raise_button").prop("disabled", true);
    $("#raise_value_button").prop("disabled", true);
    $("#bet_value_button").prop("disabled", true);


}


function ajaxIsGameEnded()
{
    //todo:servlet for is game ended...
}


function setGameStarted()
{
    setUiGameStartedMode();
    setInterval(ajaxGetPlayersTableInfo,refreshRate); //refresh players' data in the table


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
    $("#player1").empty();
    $("#player2").empty();
    $("#player3").empty();
    $("#player4").empty();
    $("#player5").empty();
    $("#player6").empty();


    var index=0;
    $.each(players || [], function (index,player_detail) {

        //  <img id="table" src="../Pages/table.png" />

        var card1_src= "../Common/cards/"+player_detail.playerCards[0]+".png";
        var card2_src= "../Common/cards/"+player_detail.playerCards[1]+".png";



        $('<table>'+'<tr>'+'<th>'+'Name:'+'</th>'+'<td>'+player_detail.playerName+'</td>'+'</tr>'

                +'<tr>'+'<th>'+'Title:'+'</th>'+'<td>'+player_detail.playerTitle+'</td>'+'</tr>'

                    +'<tr>'+'<th>'+'Tokens:'+'</th>'+'<td>'+player_detail.tokens+'</td>'+'</tr>'

                    +'<tr>'+'<th>'+'Current Bet'+'</th>'+'<td>'+player_detail.currentBet+'</td>'+'</tr>'

                    +'<tr>'+'<th>'+'Last Action'+'</th>'+'<td>'+player_detail.lastAction+'</td>'+'</tr>'+'</table>').appendTo($(players_array[index]));



        $('<img />').addClass("card1").attr('src', card1_src).appendTo($(players_array[index]));
        $('<img />').addClass("card2").attr('src', card2_src).appendTo($(players_array[index]));

                index=index+1;


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


function UpdateTableInfo(res)
{
    $(board_cards[0]).attr('src', "../Common/cards/"+res.cards[0]+".png");
    $(board_cards[1]).attr('src', "../Common/cards/"+res.cards[1]+".png");
    $(board_cards[2]).attr('src', "../Common/cards/"+res.cards[2]+".png");
    $(board_cards[3]).attr('src', "../Common/cards/"+res.cards[3]+".png");
    $(board_cards[4]).attr('src', "../Common/cards/"+res.cards[4]+".png");

    $("#pot_value").attr('value',res.pot);

}


function ajaxGetBoardInfo()
{
    $.ajax({

        url:"/GameRoom/tableInfo",
        type:'GET',
        success: function(res){

            UpdateTableInfo(res);
        }


    });

}


function setHandStarted()
{

    setUiHandStarted(); //update data in UI to hand started mode
    setInterval(ajaxIsHandEnded,refreshRate);
    setInterval(ajaxGetPlayersGameStatus,refreshRate); //refresh players data in the board
    setInterval(ajaxGetBoardInfo,refreshRate); //refresh the data on the table
    setInterval(ajaxIsMyTurn,refreshRate);

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
        $("#ready_button").attr('value',"I'm Ready");
    }
    else
    {
        is_ready="true";
        $("#ready_button").attr('value',"Not Ready");

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

function ajaxGetPlayersTableInfo()
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

    $("#ready_button").prop('disabled',true);
    $("#buy_tokens_buttons").prop('disabled',true);
    $("#fold_button").prop('disabled',true);
    $("#call_button").prop('disabled',true);
    $("#bet_button").prop('disabled',true);
    $("#check_button").prop('disabled',true);
    $("#raise_button").prop('disabled',true);
    $("#exit_game_button").prop('disabled',false);

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