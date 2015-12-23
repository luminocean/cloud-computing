var fs = require('fs');

console.log('crawler starts...');

var page = require('webpage').create();

var urlTemplate = 'https://www.semanticscholar.org/search?q=KEYWORD&page=PAGE_NUM'
urlTemplate = urlTemplate.replace(/KEYWORD/,'linear%20regression');

var finalResults = [];
var pageNum = 23, i = 0, requiredArticleNum = 200;
(function loop(){
    var url = urlTemplate.replace(/PAGE_NUM/,i);
    processPage(url, function(articleCites){
        finalResults = finalResults.concat(articleCites);

        i++;
        if(i<pageNum){
            loop();
        }else{
            // 清洗结果集
            finalResults = finalResults.filter(function(result){
                var pass = false;
                for(var attr in result){
                    if(!result[attr] || result[attr] === 'undefined') return false;
                    pass = true;
                }
                return pass;
            });
            if(finalResults.length < 200){
                console.log('cite data insufficient');
            }else{
                finalResults = finalResults.splice(0,200);
            }
            over(finalResults);
        }
    });
})();

// 将获取的结果输出到文本文件中
function over(resultDataSet){
    console.log('Result set size:' + resultDataSet.length);

    var formats = ['BibTex','MLA','APA','Chicago'];
    // 各个文件内容初始化
    var fileContents = {}; // 每种格式一个属性，值为文本内容
    for (var i = 0; i < formats.length; i++) {
        fileContents[formats[i]] = '';
    }

    // 处理数据集
    for (var j = 0; j < resultDataSet.length; j++) {
        // 单个文献
        var dataItem = resultDataSet[j];
        // 每种格式
        for (var i = 0; i < formats.length; i++) {
            var format = formats[i];
            fileContents[format] += dataItem[format];
            fileContents[format] += '\n\n';
        }
    }

    for (var i = 0; i < formats.length; i++) {
        var format = formats[i];

        fs.write(format+'.txt', fileContents[format], 'w');
    }

    phantom.exit();
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

                            var firstBtn = action.find('li > a:eq(0)');
                            // 跳过坑
                            if(firstBtn.text() === 'External Link'){
                                console.log('passed invalid result.');
                                continue;
                            }
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
