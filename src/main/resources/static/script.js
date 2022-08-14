var alreadyLoaded = false;
function initializeCheckboxes() {
    $("#container > input").checkboxradio({
        icon: false
    });
}

$(window).on('load', function() {
    initializeCheckboxes();
});

function compareResult(response) {
    $("#first-file-name").text(response.firstSummary.fileName)
    $("#first-total").text(response.firstSummary.total)
    $("#first-matched").text(response.firstSummary.matched)
    $("#first-similar").text(response.firstSummary.similar)
    $("#first-unmatched").text(response.firstSummary.unmatched)

    $("#second-file-name").text(response.secondSummary.fileName)
    $("#second-total").text(response.secondSummary.total)
    $("#second-matched").text(response.secondSummary.matched)
    $("#second-similar").text(response.secondSummary.similar)
    $("#second-unmatched").text(response.secondSummary.unmatched)

    $("#summary").show();
    var grid_selector = "#grid-table";
    if (alreadyLoaded) {
        $(grid_selector).GridUnload();
    }
    if (response.unmatchedRecords && response.unmatchedRecords.length != 0) {
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            data: response.unmatchedRecords,
            height: 450,
            colNames: [
                'ID',
                'Type',
                'Date',
                'Amount',
                'Narrative',
                'Reference',
                'ID',
                'Type',
                'Date',
                'Amount',
                'Narrative',
                'Reference'],
            colModel: [
                {name: 'first.id', index: 'first.id', width: 130},
                {name: 'first.type', index: 'first.type', width: 40},
                {name: 'first.date', index: 'first.date', width: 130},
                {name: 'first.amount', index: 'first.amount', width: 50},
                {name: 'first.narrative', index: 'first.narrative', width: 160},
                {name: 'first.walletReference', index: 'first.walletReference', width: 160},
                {name: 'second.id', index: 'second.id', width: 130},
                {name: 'second.type', index: 'second.type', width: 40},
                {name: 'second.date', index: 'second.date', width: 130},
                {name: 'second.amount', index: 'second.amount', width: 50},
                {name: 'second.narrative', index: 'second.narrative', width: 160},
                {name: 'second.walletReference', index: 'second.walletReference', width: 160},
            ],
            loadonce: true,
            altRows: true,
            caption: "Similar and unmatched items",
            autowidth: true,
            shrinkToFit: true,
            loadComplete: function () {
                var table = this;
            }
        });
        alreadyLoaded = true;
        jQuery(grid_selector).jqGrid('setGroupHeaders', {
            useColSpanStyle: false,
            groupHeaders: [
                {
                    startColumnName: 'first.id',
                    numberOfColumns: 6,
                    titleText: '<span id="header-first-file-name"></span>'
                },
                {
                    startColumnName: 'second.id',
                    numberOfColumns: 6,
                    titleText: '<span id="header-second-file-name"></span>'
                }
            ]
        });
        $("#header-first-file-name").text(response.firstSummary.fileName)
        $("#header-second-file-name").text(response.secondSummary.fileName)
    }
}

$(window).bind('resize', function () {
    //set to 0 so grid does not continually grow
    $('#grid-table').setGridWidth(0);
    //resize to our container's width
    $('#grid-table').setGridWidth($('#grid-table-wrapper').width());
}).trigger('resize');

$(document).ready(function () {
    $("#summary").hide();
    $("#but_upload").click(function () {
        var similarityFields =  [];
        $("#container > input").each(function(obj){
            if ($(this).is(':checked')) {
                similarityFields.push($(this).val());
            }

        });
        var fd = new FormData();
        var file1 = $('#firstFile')[0].files;
        var file2 = $('#secondFile')[0].files;

        // Check file selected or not
        if (file1.length > 0 && file2.length > 0) {
            fd.append('firstFile', file1[0]);
            fd.append('secondFile', file2[0]);
            fd.append("similarity-fields", similarityFields)
            $.ajax({
                url: '/compare',
                type: 'post',
                data: fd,
                contentType: false,
                processData: false,
                success: function (response) {
                    if (response != 0) {
                        compareResult(response);
                    } else {
                        alert('file not uploaded');
                    }
                },
                error: function (request, status, error) {
                    alert(request.responseText);
                }
            });
        } else {
            alert("Please select a file.");
        }
    });
    $.get("/field-matchers", function (data) {
        data.forEach(function (obj) {
            $('#container').append(
                $(document.createElement('input')).prop({
                    id: obj.id,
                    value: obj.id,
                    name: obj.description,
                    type: 'checkbox',
                    checked: true
                })
            ).append(
                $(document.createElement('label')).prop({
                    for: obj.id
                }).html(obj.description)
            );
        });
    });
});


$(function () {
    $(".controlgroup-vertical").controlgroup({
        "direction": "horizontal"
    });
});