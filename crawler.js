console.log('crawler starts...');

var page = require('webpage').create();
page.open('https://www.semanticscholar.org/search?q=linear%20regression', function() {
    console.log('web page fetched');
    page.includeJs("http://code.jquery.com/jquery-2.1.4.min.js", function() {
        var listeningRepaint = true;
        var citeClicked = false;

        page.onRepaintRequested = waitForArtiles;

        function waitForArtiles(){
            if(!listeningRepaint) return;

            var prepared = page.evaluate(function(){
                if($("article").length > 0){
                    return true;
                }
            });
            if(!prepared) return;

            listeningRepaint = false;
            page.onRepaintRequested = handleCites;
            listeningRepaint = true;
        }

        function handleCites(){
            if(!listeningRepaint) return;

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

                var modal = $('.modal-container').get(0);
                return modal.innerHTML;
            });

            console.log(citeContent);
        }

    });

    // phantom.exit();
});
