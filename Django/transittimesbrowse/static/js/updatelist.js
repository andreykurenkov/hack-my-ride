console.log('Loading text update script')
$(document).ready(function() {
    $('#text_search').bind('input',function(){
        $.get(window.location.pathname, {'search':$(this).val()},function(data){
            $('#list').html(data);
        });
    });

   $("#text_search").keypress(function(event) {
    if(event.which == '13') {
      return false;
    }
  });
});
