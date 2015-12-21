console.log('crawler starts...');

var page = require('webpage').create();

var urlTemplate = 'https://www.semanticscholar.org/search?q=KEYWORD&page=PAGE_NUM'
urlTemplate = urlTemplate.replace(/KEYWORD/,'linear%20regression');

var finalResults = [];
var pageNum = 3, i = 0;
(function loop(){
    var url = urlTemplate.replace(/PAGE_NUM/,i);
    processPage(url, function(articleCites){
        finalResults = finalResults.concat(articleCites);

        i++;
        if(i<pageNum){
            loop();
        }else{
            over();
        }
    });
})();

function over(){
    console.log(finalResults.length);
}

function processPage(url, done){
    page.open(url, function() {
        console.log('web page fetched:' + url);
        page.includeJs("http://code.jquery.com/jquery-2.1.4.min.js", function() {
            var citeClicked = false;
            var citeProcessed = false;

            // page内消息监听
            page.onConsoleMessage = function(msg) {
                console.debug(msg);
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
                    var articleCites = page.evaluate(function(){
                        var paperActions = $("article > .paper-actions");

                        var articleCites = [];

                        // 遍历每个搜索结果
                        for (var count = 0; count < paperActions.length; count++) {
                            console.log('---------------------------------');
                            console.log('Result No.'+count);

                            var action = $(paperActions.get(count));

                            var citeBtn = action.find('li > a:eq(1)');
                            citeBtn[0].click(); // 点击site按钮

                            // 获取模态框里所有的格式按钮
                            var modal = $($('.modal-container').get(0));
                            var formatBtns = [];
                            var btns = modal.find('.tabs button');
                            for (var i = 0; i < btns.length; i++) {
                                var btn = $(btns.get(i));
                                formatBtns.push(btn);
                            }

                            var articleCite = {};

                            // 依次点击每种格式并获取信息
                            for (var i = 0; i < formatBtns.length; i++) {
                                var btn = formatBtns[i];
                                btn[0].click();

                                var cite = modal.find('cite:eq(0)');
                                articleCite[btn.text()] = cite.text();

                                console.log('['+btn.text()+']'+cite.text());
                            }

                            articleCites.push(articleCite);
                        }

                        return articleCites;
                    });

                    citeClicked = true;
                    done(articleCites);
                }
            }
        });

        // phantom.exit();
    });
}
