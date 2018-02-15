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



$(function(){

    ajaxGetGameDetails();

});