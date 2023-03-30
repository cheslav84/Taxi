$(document).ready(function(){
  if(localStorage) {
    var lastActiveId = localStorage.getItem("lastActiveNavId");
    var element = document.getElementById(lastActiveId);
    element.parentElement.classList.add("current_page_item");
  } 

});

function setActive(clicked_id) {  
    localStorage.setItem("lastActiveNavId", clicked_id);
}