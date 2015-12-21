console.log('crawler starts...');

var page = require('webpage').create();
page.open('https://www.semanticscholar.org/search?q=linear%20regression', function() {
    console.log('web page fetched');
    page.includeJs("http://code.jquery.com/jquery-2.1.4.min.js", function() {
        var citeClicked = false;

        // page内消息监听
        page.onConsoleMessage = function(m1, m2) {
            console.log('From page: %s,%s', m1, m2);
        };

        // 重画监听
        page.onRepaintRequested = waitForArtiles;

        function waitForArtiles(){
            var prepared = page.evaluate(function(){
                if($("article").length > 0){
                    return true;
                }
            });
            if(!prepared) return;

            page.onRepaintRequested = handleCites;
        }

        function handleCites(){
            // 只点击一次
            if(!citeClicked){
                page.evaluate(function(){
                    var paperActions = $("article > .paper-actions");
                    var action = $(paperActions.get(0));
                    var citeBtn = action.find('li > a:eq(1)');
                    citeBtn[0].click();
                });

                citeClicked = true;
                console.log('cite clicked');
            }

            var citeContent = page.evaluate(function(){
                if(!$('.modal-container').get(0)) return;

                var modal = $($('.modal-container').get(0));
                return modal.html();
            });

            console.log(citeContent);
        }

    });

    // phantom.exit();
});
