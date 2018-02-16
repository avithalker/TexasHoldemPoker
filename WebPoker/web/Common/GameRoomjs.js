var refreshRate=2000;
var is_ready="false";
var is_game_started="false";
var is_hand_started="false"
var interval_id_hand_started;
var interval_id_game_started;
var interval_id_my_turn;
var bet_value;
var raise_value;





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
            if(r.isSucceed == true)
            {
                is_game_started="true";
            }

        }
    });

    if(is_game_started=="true")
    {
        clearInterval(interval_id_game_started);
        setGameStarted();

    }
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

}


function setGameStarted()
{
    setUiGameStartedMode();
    setInterval(ajaxGetPlayersInfo(),refreshRate); //refresh players table

    //question we need to ask during "game has started" mode:
    setInterval(ajaxIsHandStarted(),refreshRate);

}


function setHandStarted()
{

    setUiHandStarted(); //update data in UI to hand started mode
    setInterval(ajaxIsMyTurn(),refreshRate);

}

function ajaxIsHandStarted()
{
    $.ajax({
        url: "/GameRoom/isHandStarted",
        success: function(r) {
            if(r.isSucceed == true)
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
        $("#ready_button").value("Not Ready");
    }
    else
    {
        is_ready="true";
        $("#ready_button").value("Ready");

    }
    $.ajax(
        {
            url:"/GameRoom/readyToStart",
            type:'POST',
            data: "isReady="+is_ready,
            dataType:"json",
            success:function(r){

                if(r.isSuccess==false)
                {
                    window.alert(r.msgError);
                }
            }

        }
    );

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
    $("#exit_game_button").disabled=

}

$(function(){

    ajaxGetGameDetails();
    initializeButtons();

    //override click action buttons
    $("#ready_button").click(function(){ajaxReadyToStart();});
    $("#buy_tokens_button").click(function(){ajaxBuyTokens();});
    $("#fold_button").click(function(){ajaxFoldAction();});
    $("#call_button").click(function(){ajaxCallAction();});
    $("#bet_button").click(function(){ajaxBetAction();});
    $("#check_button").click(function(){ajaxCheckAction();});
    $("#raise_button").click(function(){ajaxRaiseAction();});


    interval_id_game_started=setInterval(ajaxIsGameStarted(),refreshRate);
    //here the game  already started


});