var zipCode = 90210;

$.ajax({
    dataType: "json",
    headers:  { "Accept": "application/json; odata=verbose" },
    url: "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D"+zipCode+"%20limit%201&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys",
    beforeSend: function(xhr){xhr.setRequestHeader('Accept', 'application/json; odata=verbose');},
    success: function(data){
        $.getJSON("https://query.yahooapis.com/v1/public/yql?callback=?", {
            q: "select * from xml where url=\"https://weather.yahooapis.com/forecastrss?w="+data.query.results.place.locality1.woeid+"\"",
            format: "json"
        },function (data) {
          var weather = data.query.results.rss.channel;
          var html = '<div><span class="temperature">'+weather.item.condition.temp+'<span class="degree">&deg;</span><sup>'+weather.units.temperature+'</sup></span><br><span class="wind-chill">Feels like: '+weather.wind.chill+'<span class="degree">&deg;</span></span></div></a>';
                  $("#weather").html(html);
        });
    },
});