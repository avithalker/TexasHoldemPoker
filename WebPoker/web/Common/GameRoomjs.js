var refreshRate=2000;
var is_ready="false";
var is_game_started="false";
var is_hand_started="false";
var interval_id_hand_started;
var interval_id_hand_ended;
var interval_id_game_started;
var interval_id_game_ended;
var interval_id_my_turn;
var interval_id_board_info;
var interval_id_game_details;
var interval_id_players_game_status;
var bet_value;
var raise_value;
var computer=false;
var players_array=["#player1","#player2","#player3","#player4","#player5","#player6"];
var board_cards=["#card_board1","#card_board2","#card_board3","#card_board4","#card_board5"];




function ajaxGetMyDetails() {
    $.ajax({
        url: "../Lobby/MyDetails",
        success: function (myDetails) {
            $("#playerName").text("Player name: " + myDetails.playerName);
        }
    });
}

function ajaxExitRoom() {
    $.ajax(
        {
            url: "../GameRoom/exitRoom",
            type: 'POST',
            success: function () {
                location.href = "./LobbyPage.html";
            }
        }
    );
}

function ajaxGetGameDetails()
{
    $.ajax(
        {
            url:"../GameRoom/gameInfo",
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
    $("#gameName").text("Game name: "+gamedetails.gameName);

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
        url: "../GameRoom/isGameStarted",
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
        url:"../GameRoom/buyTokens",
        type:'POST',
        success: function (res) {

            if(res.isSucceed==false)
            {
                window.alert(res.msgError);
            }
        }
    });
}

function setUiGameStartedMode()
{
    $("#ready_button").attr("disabled",false);
    $("#buy_tokens_button").attr("disabled",false);
    $("#exit_game_button").attr("disabled",false);
    $("#fold_button").attr("disabled",true);
    $("#call_button").attr("disabled",true);
    $("#check_button").attr("disabled",true);
    $("#bet_button").attr("disabled",true);
    $("#raise_button").attr("disabled",true);
    $("#raise_value_button").attr("disabled",true);
    $("#bet_value_button").attr("disabled",true);
}

function ajaxIsGameEnded()
{
    //todo:servlet for is game ended...
    $.ajax({
        url:"../GameRoom/isGameEnded",
        type:'GET',
        success: function (res) {

            if(res.result==true)
            {
                clearInterval(interval_id_game_ended);
                handleEndOfGame(res.msg);
            }
        }
    });
}

function handleEndOfGame(endReason) {
    alert('Game has ended. Reason: ' + endReason);
    ajaxExitRoom();
}


function setGameStarted()
{
    alert('Game started- all players are in');
    setUiGameStartedMode();
    setInterval(ajaxGetPlayersTableInfo,refreshRate); //refresh players' data in the table
    interval_id_game_details = setInterval(ajaxGetGameDetails,refreshRate);


    //question we need to ask during "game has started" mode:
    interval_id_hand_started = setInterval(ajaxIsHandStarted,refreshRate);
    interval_id_game_ended = setInterval(ajaxIsGameEnded,refreshRate);
}


function ajaxIsHandEnded()
{
    $.ajax({

        url:"../GameRoom/isHandRoundEnded",
        type:'GET',
        success: function(res){

            if(res.result==true)
            {
              clearInterval(interval_id_hand_ended);
              clearInterval(interval_id_my_turn);
              clearInterval(interval_id_board_info);
              clearInterval(interval_id_players_game_status);
              setHandEndedMode(res.msg);
            }
        }

    });
}

function ajaxGetWinners()
{
    $.ajax({

        url:"../GameRoom/winners",
        type:'GET',
        success: function(winners){

            $.each(winners || [], function (index,winner_detail){

                $('<tr>'+'<td>'+winner_detail.playerName+'</td>'+'<td>'+winner_detail.playerType+'</td>'+'<td>'+winner_detail.totalBuys+
                    '</td>'+'<td>'+winner_detail.winningPrice+'</td>'+'<td>'+winner_detail.totalWinsNumber+'</td>'+'<td>'
                    +winner_detail.totalHandsPlayed+'</td>'+'<td>'+winner_detail.handRank+'</td>'+'</tr>').appendTo($("#winners_table"));

            });

            $(".Board").hide();
            $("#Winners").show();

        }

});

}


function setHandEndedMode(reason)
{
    alert('Hand Has Ended! Reason: '+ reason);
    //GetWinnersrequest
    ajaxGetWinners();
    //restart new hand
    setUiGameStartedMode();
    interval_id_game_ended = setInterval(ajaxIsGameEnded,refreshRate);
    interval_id_hand_started = setInterval(ajaxIsHandStarted,refreshRate);
    interval_id_game_details = setInterval(ajaxGetGameDetails,refreshRate);

}

function setUiHandStarted()
{
    $("#Winners").hide();
    $('.Board').show();
    $("#ready_button").attr("disabled",true);
    $("#buy_tokens_button").attr("disabled",true);
    $("#exit_game_button").attr("disabled",true);

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
        if(player_detail.isActive==false)
        {
            var card1_src= "../Common/cards/notactive.png";
            var card2_src= "../Common/cards/notactive.png";
        }
        else if(player_detail.lastAction=="Fold")
        {
            var card1_src= "../Common/cards/fold.png";
            var card2_src= "../Common/cards/fold.png";
        }
        else
        {

            var card1_src= "../Common/cards/"+player_detail.playerCards[0]+".png";
            var card2_src= "../Common/cards/"+player_detail.playerCards[1]+".png";

        }

        $('<table></table>')
            .addClass('tableplayer')
            .append(
                $('<tr></tr>')
                    .append(
                        $('<th></th>').text('Name:'),
                        $('<td></td>').text(player_detail.playerName)
                    ),
                $('<tr></tr>')
                    .append(
                        $('<th></th>').text('Title:'),
                        $('<td></td>').text(player_detail.playerTitle)
                    ),
                $('<tr></tr>')
                    .append(
                        $('<th></th>').text('Tokens:'),
                        $('<td></td>').text(player_detail.tokens)
                    ),
                $('<tr></tr>')
                    .append(
                        $('<th></th>').text('Current Bet:'),
                        $('<td></td>').text(player_detail.currentBet)
                    ),
                $('<tr></tr>')
                    .append(
                        $('<th></th>').text('Last Action:'),
                        $('<td></td>').text(player_detail.lastAction)
                    )
            ).appendTo($(players_array[index]));

        $('<img />').addClass("card1").attr('src', card1_src).appendTo($(players_array[index]));
        $('<img />').addClass("card2").attr('src', card2_src).appendTo($(players_array[index]));


        index=index+1;


    });
}

function ajaxGetPlayersGameStatus()
{
    $.ajax({

        url:"../GameRoom/PlayersGameStatus",
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

    $("#pot_value").text(res.pot);

}


function ajaxGetBoardInfo()
{
    $.ajax({

        url:"../GameRoom/tableInfo",
        type:'GET',
        success: function(res){

            UpdateTableInfo(res);
        }
    });

}


function setHandStarted()
{

    cleanWinnersTable();
    cleanIsReadyStatus();
    setUiHandStarted(); //update data in UI to hand started mode
    interval_id_hand_ended=setInterval(ajaxIsHandEnded,refreshRate);
    interval_id_players_game_status=setInterval(ajaxGetPlayersGameStatus,refreshRate); //refresh players data in the board
    interval_id_board_info=setInterval(ajaxGetBoardInfo,refreshRate); //refresh the data on the table
    interval_id_my_turn=setInterval(ajaxIsMyTurn,refreshRate);


}

function ajaxIsHandStarted()
{
    $.ajax({
        url: "../GameRoom/isHandStarted",
        success: function(r) {
            if(r.result == true)
            {
                clearInterval(interval_id_hand_started);
                clearInterval(interval_id_game_details);
                clearInterval(interval_id_game_ended);
                setHandStarted();
            }
        }
    });
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
            url:"../GameRoom/readyToStart",
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
    if(computer==true)
    {

        $.ajax({

            url:"../GameRoom/makeAction",
            type:'POST',
            data:"action="+'0'+'&value='+'0',
            success: function(r){
                if(r.isSucceed==false)
                {
                    window.alert(r.msgError);
                }
            }

        });

    }
    else {
        $.ajax({

            url: "../GameRoom/validPokerActions",
            type: 'GET',
            success: function (r) {
                $("#raise_button").attr("disabled", !(r.isRaiseValid));
                $("#raise_value_button").attr("disabled", !(r.isRaiseValid));
                $("#bet_value_button").attr("disabled", !(r.isBetValid));
                $("#bet_button").attr("disabled", !(r.isBetValid));
                $("#call_button").attr("disabled", !(r.isCallValid));
                $("#check_button").attr("disabled", !(r.isCheckValid));
                $("#fold_button").attr("disabled", !(r.isFoldValid));
            }
        });
    }

    interval_id_my_turn=setInterval(ajaxIsMyTurn,refreshRate);

}

function  disableActionButtons()
{
    $("#raise_button").attr("disabled",true);
    $("#raise_value_button").attr("disabled",true);
    $("#bet_value_button").attr("disabled",true);
    $("#bet_button").attr("disabled",true);
    $("#call_button").attr("disabled",true);
    $("#check_button").attr("disabled",true);
    $("#fold_button").attr("disabled",true);

}


function ajaxIsMyTurn()
{
    $.ajax({

        url:"../GameRoom/isMyTurn",
        type:'GET',
        success: function(r){

            if(r.result==true)
            {
               clearInterval(interval_id_my_turn);
               setUiMyTurn(); //set the ui to my_turn mode;
            }
            else{
                disableActionButtons();
            }

        }
    });
}


function ajaxFoldAction()
{
    $.ajax({

        url:"../GameRoom/makeAction",
        type:'POST',
        data:"action="+'4'+'&value='+'0',
        success: function(r){
            if(r.isSucceed==false)
            {
                window.alert(r.msgError);
            }
        }

    });
    disableActionButtons();
}

function ajaxCheckAction()
{
    $.ajax({
        url:"../GameRoom/makeAction",
        type:'POST',
        data:"action="+'2'+'&value='+'0',
        success: function(r){
            if(r.isSucceed==false)
            {
                window.alert(r.msgError);
            }
        }

    });
    disableActionButtons();
}


function ajaxBetAction()
{
    bet_value=$("#bet_value_button").val();

    $.ajax({

        url:"../GameRoom/makeAction",
        type:'POST',
        data:"action="+'1'+'&value='+bet_value,
        success: function(r){
            if(r.isSucceed==false)
            {
                window.alert(r.msgError);
            }
            else
            {
                $("#bet_value_button").val('');
            }
        }

    });
    disableActionButtons();
}


function ajaxRaiseAction()
{
    raise_value=$("#raise_value_button").val();

    $.ajax({

        url:"../GameRoom/makeAction",
        type:'POST',
        data:"action="+'3'+'&value='+raise_value,
        success: function(r){
            if(r.isSucceed==false)
            {
                window.alert(r.msgError);
            }
            else
            {
                $("#raise_value_button").val('');
            }
        }

    });
    disableActionButtons();
}


function ajaxCallAction()
{
    $.ajax({

        url:"../GameRoom/makeAction",
        type:'POST',
        data: "action="+'5'+'&value='+'0',
        success: function(r){
            if(r.isSucceed==false)
            {
                window.alert(r.msgError);
            }
        }

    });
    disableActionButtons();
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

        url:"../GameRoom/PlayersInfo",
        type:'GET',
        success: function(res){
            setPlayersTable(res);
        }


    })
}



function  initializeButtons(){

    $("#ready_button").attr("disabled",true);
    $("#buy_tokens_buttons").attr("disabled",true);
    $("#fold_button").attr("disabled",true);
    $("#call_button").attr("disabled",true);
    $("#bet_button").attr("disabled",true);
    $("#check_button").attr("disabled",true);
    $("#raise_button").attr("disabled",true);
    $("#exit_game_button").attr("disabled",false);

}

function cleanWinnersTable(){
    $("#winners_table").find("tr:gt(0)").remove();
}

function cleanIsReadyStatus() {
    is_ready="false";
    $("#ready_button").attr('value',"I'm Ready");
}



function ajaxPlayerType()
{
    $.ajax({

        url: "../GameRoom/isComputerType",
        type: 'GET',
        success: function(res){

            computer=res.result;
        }

    });

}



$(function(){

    $(".Board").hide();
    $("#Winners").hide();

    initializeButtons();
    ajaxPlayerType();
    ajaxGetGameDetails();
    ajaxGetMyDetails();
    //get player's type.
    //override click action buttons
    $("#ready_button").click(function(){ajaxReadyToStart();});
    $("#buy_tokens_button").click(function(){ajaxBuyTokens();});
    $("#fold_button").click(function(){ajaxFoldAction();});
    $("#call_button").click(function(){ajaxCallAction();});
    $("#bet_button").click(function(){ajaxBetAction();});
    $("#check_button").click(function(){ajaxCheckAction();});
    $("#raise_button").click(function(){ajaxRaiseAction();});
    interval_id_game_started=setInterval(ajaxIsGameStarted,refreshRate);

});