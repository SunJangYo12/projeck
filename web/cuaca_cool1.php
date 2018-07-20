<?php 
date_default_timezone_set('CET');
class weatherfc{
public $result;

function weather($city){
 $BASE_URL = "http://query.yahooapis.com/v1/public/yql";
 $yql_query = 'select * from weather.forecast where woeid in (select woeid from geo.places(1) where text="'.$city.'") and u="c"';
 $yql_query_url = $BASE_URL . "?q=" . urlencode($yql_query) . "&format=json";
    // Make call with cURL
    $session = curl_init($yql_query_url);
    curl_setopt($session, CURLOPT_RETURNTRANSFER,true);
    $json = curl_exec($session);
    // Convert JSON to PHP object
    $phpObj =  json_decode($json);
    //var_dump($phpObj);
    $weatherd='<div> Weather In '.$city.'<br>';
    $fcast=$phpObj->query->results->channel->item->forecast;
    
    foreach($fcast as $witem){
       $fdate=DateTime::createFromFormat('j M Y', $witem->date);

       $weatherd.= '<div class="days">';
       $weatherd.= '<div class="item"><div>'.$fdate->format('d.m').'&nbsp;'.$witem->day.'</div><div class="image" style="width:90px !important; height:65px !important;"><img src="http://us.i1.yimg.com/us.yimg.com/i/us/nws/weather/gr/'.$witem->code.'d.png" width=90></div></div>';
       $weatherd.= '<div><span>'.$witem->high.'&deg;C</span>';
       $weatherd.= '<span>'.$witem->low.'°C</span></div></div>';
    };
    $this->result=$weatherd;
}

}

$h = new weatherfc;
$h->weather("Indonesia,Watumalang");
echo $h->result;
?>



    <style>
    .days{
    width:90px;
    font-size:12px;
    float:left;
    font-family:Arial, Helvetica, sans-serif;
    border:#999 1px dotted;
    }

</style>
