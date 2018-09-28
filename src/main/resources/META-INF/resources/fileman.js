function Fileman() {
    var loading = false;
    var xhr;

    var fileURI = window.location.hash.substring(1);
    var pageNo = 0;
    var pageSize = 36;

    this.init = function () {
        if (!fileURI) return;
        document.title = "Fileman - " + fileURI;
        this.load(fileURI, pageNo, pageSize, this.onXHRStateChanged);
    };

    this.load = function (_fileURI, _pageNo, _pageSize, _onXHRStateChanged) {
        if (loading) return;
        else loading = true;

        fileURI = _fileURI;
        pageNo = _pageNo;
        pageSize = _pageSize;

        if (window.XMLHttpRequest) {// code for all new browsers
            xhr = new XMLHttpRequest();
        } else if (window.ActiveXObject) {// code for IE5 and IE6
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }

        if (xhr != null) {
            xhr.onreadystatechange = _onXHRStateChanged;
            xhr.open("GET", fileURI, true);
            var start = pageNo * pageSize;
            var end = (pageNo + 1) * pageSize;
            xhr.setRequestHeader("Range", "lines=" + start + "-" + end);
            xhr.send(null);
        } else {
            alert("Your browser does not support XMLHTTP.");
        }
    };

    this.prev = function () {
        this.load(fileURI, pageNo >= 1 ? pageNo - 1 : pageNo, pageSize, this.onXHRStateChanged);
    };

    this.next = function () {
        this.load(fileURI, pageNo + 1, pageSize, this.onXHRStateChanged);
    };

    this.show = function (_pageNo) {
        this.load(fileURI, _pageNo, pageSize, this.onXHRStateChanged);
    };

    this.onXHRStateChanged = function () {
        if (xhr.readyState !== 4) return;

        loading = false;

        if (xhr.status >= 200 && xhr.status < 300) {
            document.getElementById("content").innerText = xhr.responseText;
            var range = xhr.getResponseHeader("Content-Range");
            var total = parseInt(range.substring(range.indexOf('/') + 1));
            var pages = parseInt(total / pageSize) + (total % pageSize === 0 ? 0 : 1);

            var html = '\n';
            html += '<button type="button" class="page" onclick="fileman.prev();" ' + (pageNo === 0 ? 'disabled' : '') + '>Prev</button>\n';
            var left = false;
            var right = false;
            for (var i = 0; i < pages; i++) {
                if (Math.abs(pageNo - i) < 4 || i === 0 || i === pages - 1) {
                    html += '<button type="button" class="page' + (i === pageNo ? ' current' : '') + '" ' + (pageNo === i ? 'disabled' : '') + ' onclick="fileman.show(' + i + ');">' + (i + 1) + '</button>\n';
                } else if (pageNo > i && !left) {
                    html += '...\n';
                    left = true;
                } else if (pageNo < i && !right) {
                    html += '...\n';
                    right = true;
                }
            }
            html += '<button type="button" class="page" onclick="fileman.next();"' + (pageNo === (pages - 1) ? 'disabled' : '') + '>Next</button>\n';

            document.getElementById("pagination").innerHTML = html;
        }
        else {
            alert("Problem retrieving data");
        }
    };
}

window.fileman = new Fileman();
fileman.init();