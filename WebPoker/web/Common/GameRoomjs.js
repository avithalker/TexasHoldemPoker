var refreshRate=2000;
var is_ready="false";
var is_game_started="false";
var is_hand_started="false"
var interval_id_hand_started;
var interval_id_game_started;
var interval_id_my_turn;





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
        setInterval(ajaxIsHandStarted(),refreshRate);
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

function setGameStarted()
{

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


$(function(){

    ajaxGetGameDetails();

    //override click action buttons
    $("#ready_button").click(function(){ajaxReadyToStart();});
    $("#buy_tokens_button").click(function(){ajaxBuyTokens();});

    interval_id_game_started=setInterval(ajaxIsGameStarted(),refreshRate);
    //here the game  already started


});