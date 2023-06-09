"use strict";

$(document).on('ready', function() { 

	initParallax();
	initEvents();
	initMasonry();
	// initMap();
	initCollapseMenu();
	initContactForm();
	toggleAccordeon();
	
	/* You can disable srollanimation by removing next function */
	checkScrollAnimation();
	$(function() { $('.matchHeight').matchHeight(); });	
	$(".accordion").accordion();
	$(".tabs-ui").tabs();

	/* Lightbox plugin */
	$('.swipebox').swipebox();

	$('.navbar-affix').affix({
	      offset: {
	        top: $('#top-bar').height(),
	      }
	});

	initSwiper();
});

$(window).on('scroll', function (event) {

	checkNavbar();
	checkCountUp();
	checkScrollAnimation();
	initProgressBar();
}).scroll();

/* Parallax fix on window resize */ 
$(window).on('resize', function(){

 	initParallax();
});

$(window).on('load', function(){

	initMasonry()
});

/* Collapse menu slide */
function initCollapseMenu() {

	var navbar = $('#navbar'),
		navbar_toggle = $('.navbar-toggle'),
		navbar_wrapper = $("#nav-wrapper");

    navbar_wrapper.on('click', '.navbar-toggle', function (e) {

    	console.log('!!!');

        navbar_toggle.toggleClass('collapsed');
        navbar.toggleClass('collapse');
    });

    navbar_wrapper.on('click', '.hasSub > a', function() {

    	var el = $(this);

    	el.next().toggleClass('show');
    	el.parent().toggleClass('show');
    	return false;
    });

    var lastWidth;
    $(window).on("resize", function () {

    	var winWidth = $(window).width();

        if (winWidth > 1199 && navbar_toggle.is(':hidden')) {
            navbar.addClass('collapse');
            navbar_toggle.addClass('collapsed');
        }

       	lastWidth = winWidth;
    });	
}


/* All keyboard and mouse events */
function initEvents() {

	$('.menu-types').on('click', 'a', function() {

		$(this).addClass('active').siblings('.active').removeClass('active');
		$(this).parent().find('.type-value').val($(this).data('value'));

		return false;
	});

	/* Scrolling to navbar from "go top" button in footer */
    $('footer').on('click', '.go-top', function() {

	    $('html, body').animate({ scrollTop: 0 }, 800);
	});
}

/* Parallax initialization */
function initParallax() {

	// Only for desktop
	if (/Mobi/.test(navigator.userAgent)) return false;

	$('.parallax').parallax("50%", 0.1);
}

/* Swiper slider initialization */
function initSwiper() {

	/* Slider for clients on main page */	
    var clientsSwiper = new Swiper('#testimonials-slider', {
		direction   : 'horizontal',

		speed		: 1000,
		nextButton	: '.arrow-right',
		prevButton	: '.arrow-left',
	
		autoplay    : 7000,
		autoplayDisableOnInteraction	: false,
    });

	/* Slider for inner pages */	
    var innerSwiper = new Swiper('.slider-inner', {
		direction   : 'horizontal',
        pagination: '.swiper-pagination',
        paginationClickable: true,		
		autoplay    : 4000,
		autoplayDisableOnInteraction	: false,        
    });

	$(window).on('resize', function(){

		var ww = $(window).width()
		if ($('#testimonials-slider').length) {

			if (ww > 1000) { clientsSwiper.params.slidesPerView = 3; }
			if (ww <= 1000) { clientsSwiper.params.slidesPerView = 2; }
			if (ww <= 479) { clientsSwiper.params.slidesPerView = 1; }		
		
			clientsSwiper.update();			
		}
	}).resize();



}

/* Masonry initialization */
function initMasonry() {

	$('.masonry').masonry({
	  itemSelector: '.item',
	  columnWidth:  '.item'
	});		
}

/* Animated progress bar */
function initProgressBar() {

	var block = $('.progressItems');

    if (block.length) {

	    var scrollTop = block.offset().top - window.innerHeight;

	    if (!block.data('counted') && $(window).scrollTop() > scrollTop) {

	    	/* Initialized once */
			$('.progressBar').each(function(i, el) {
				progressBar(parseInt($(el).find('.value').html(), 10), $(el));
			});

	    	block.data('counted', 1);
	    }  
	}
}

function progressBar(percent, $element) {
    var progressBarWidth = percent * $element.width() / 100;
    $element.find('.bar div').animate({ width: progressBarWidth }, 1000);
}

/* Scroll animation used for landing page */
function checkScrollAnimation() {

	var scrollBlock = $('#car-block');
    if (scrollBlock.length) {

	    var scrollTop = scrollBlock.offset().top - window.innerHeight;

	    if (!scrollBlock.data('done') && $(window).scrollTop() > scrollTop) {

	    	$('#car-block .animation-block img').addClass('slideleft');
	    	scrollBlock.data('done', 1);
	    }  
	}
}

/* Starting countUp function on landing page */
function checkCountUp() {

	var countBlock = $('.skills');
    if (countBlock.length) {

	    var scrollTop = countBlock.offset().top - window.innerHeight;

	    if (!countBlock.data('counted') && $(window).scrollTop() > scrollTop) {

	    	/* Initialized once */
	    	for (var x = 1; x <= 4; x++) {

				var numAnim = new CountUp('countUp-' + x, 1, $('#countUp-' + x).html());
				numAnim.start();
	    	}

	    	countBlock.data('counted', 1);
	    }  
	}
}

/* Navbar is set darker on main page on scroll */
function checkNavbar() {

	var scroll = $(window).scrollTop(),
    	navBar = $('nav.navbar'),
	    slideDiv = $('.slider-full');

    if (scroll > 1) navBar.addClass('dark'); else navBar.removeClass('dark');
}

/* Ajax Contact Form */
function initContactForm() { 

	var formEl = $('.form-validate');

	formEl.validate({

		submitHandler: function(form) {

			var queryString = $(formEl).serialize(); 
			$.post('_ajax_send.php', queryString, function(answer) {

				if (answer) {

					formEl.html('<div class="warning">' + answer + '</div>');
				}	
			}); 
		}
	});
}


function toggleAccordeon() { 

	var acc = document.getElementsByClassName("toggled-accordion");
	var i;
	for (i = 0; i < acc.length; i++) {
	  acc[i].addEventListener("click", function() {
		this.classList.toggle("active");
		var panel = this.nextElementSibling;
		if (panel.style.maxHeight) {
		  panel.style.maxHeight = null;
		} else {
		  panel.style.maxHeight = panel.scrollHeight + "px";
		} 
	  });
	}
}

const show_reg_pw_btn = document.querySelector('#show-reg-passwd')
const show_reg_pw_icon = show_reg_pw_btn.querySelector('img')
const pw_reg_input = document.querySelector('#reg-password')

show_reg_pw_btn.addEventListener('click', () => {
	pw_reg_input.type = pw_reg_input.type === 'password'
		? 'text'
		: 'password'

	$("#show-reg-passwd").toggleClass('fa-eye');
	$("#show-reg-passwd").toggleClass('fa-eye-slash');
})

const show_conf_pw_btn = document.querySelector('#show-conf-passwd')
const pw_conf_input = document.querySelector('#conf-password')

show_conf_pw_btn.addEventListener('click', () => {
	pw_conf_input.type = pw_conf_input.type === 'password'
		? 'text'
		: 'password'

	$("#show-conf-passwd").toggleClass('fa-eye');
	$("#show-conf-passwd").toggleClass('fa-eye-slash');
})


function getTripDetailes(value) {
	location.href = value;
}


