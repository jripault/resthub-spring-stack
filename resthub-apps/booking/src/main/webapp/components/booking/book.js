(function($) {

var bookBooking =
{
    options: {
		id: null,
        data : {},
        template : 'components/booking/book.html',
        context : null
    },
    _create: function() {
		this.element.addClass('bd-booking-book');
    },
    _init: function() {
		var id = this.options.id;
		var context = this.options.context;

		this.element.find('h1:first').html("Book hotel");
		$('a#cancel-request').attr('href', '/#/hotel/'+ id);
		$('a#cancel-request').html('Cancel');
		$('input#book-request').attr('value', 'Proceed');

		$('div#booking-form-fields').load('components/booking/book.html', null, function() {
			$('input[name=checkinDate]').datepicker();
			$('input[name=checkoutDate]').datepicker();
			$('form#booking-form').validate({errorElement: 'span'});
		});
		
		$('input#book-request').unbind();
		$('input#book-request').bind('click', function() {
			var result = $('form#booking-form').validate({errorElement: 'span'}).form();
			if (result) {
				var checkinDate = $('input[name=checkinDate]').val();
				var checkoutDate = $('input[name=checkoutDate]').val();

				try {
					var checkinDateTimestamp = $.datepicker.parseDate('mm/dd/yy', checkinDate).getTime();
					var checkoutDateTimestamp = $.datepicker.parseDate('mm/dd/yy', checkoutDate).getTime();
				} catch(err) {
					console.log('Bad date format...');
				}

				var secondsBetween = (checkoutDateTimestamp - checkinDateTimestamp) / 1000;
				var daysBetween = secondsBetween / 86400;

				var beds = $('select[name=beds] option:selected').val();
				var total = daysBetween * beds;

				var booking = {
					hotelId: id,
					checkinDate: checkinDate,
					checkoutDate: checkoutDate,
					days: daysBetween,
					beds: beds,
					total: total,
					smoking: $('input:checked[name=smoking]').val(),
					creditCard: $('input[name=creditCard]').val(),
					creditCardName: $('input[name=creditCardName]').val(),
					creditCardExpiryMonth: $('select[name=creditCardExpiryMonth] option:selected').val(),
					creditCardExpiryYear: $('select[name=creditCardExpiryYear] option:selected').val()
				}
				$.session.setJSONItem('booking', booking);
				// console.log(booking);
				context.redirect('#/booking/confirm');
			}
		});
		
		// $('input#book-request').click(this._proceedBookForm);
    },
	_proceedBookForm: function() {
		// TODO
	},
    destroy: function() {
        this.element.removeClass('bd-booking-book');
        $.Widget.prototype.destroy.call( this );
    }
};

$.widget("booking.bookBooking", $.resthub.resthubController, bookBooking);
})(jQuery);