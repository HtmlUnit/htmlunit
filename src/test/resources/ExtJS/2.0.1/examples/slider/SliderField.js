/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

// vim: fdc=4:nu:ts=4:sw=4:nospell
/**
  * @class Ext.form.SliderField
  * @extends Ext.form.Field
  * Slider form field. 
  * @constructor
  * Creates a new SliderField
  * @param {Object} config Configuration options
  */
Ext.form.SliderField = Ext.extend(Ext.form.Field, {
	defaultAutoCreate : {tag:'input', type:'hidden'},

	// private
	initComponent : function() {
		Ext.form.SliderField.superclass.initComponent.call(this);
		this.minValue = this.minValue || 0;
		this.maxValue = this.maxValue || 1;
	},

	// private
	onRender: function(ct, position) {
		Ext.form.SliderField.superclass.onRender.call(this, ct, position);
		this.setValue(this.getValue() || this.minValue);
		this.sbar = Ext.DomHelper.insertFirst(ct, {tag:'div', cls:'x-slider-hbar'}, true);
		this.sthumb = Ext.DomHelper.append(ct, {tag:'img', src:Ext.BLANK_IMAGE_URL, cls:'x-slider-thumb'}, true);
		this.sthumb.addClassOnOver('x-slider-thumb-over');

	},

	// private
	afterRender: function() {
		Ext.form.SliderField.superclass.afterRender.call(this);
		this.sbar.setWidth(parseInt(this.el.dom.style.width, 10));

		this.dd = new Ext.dd.DD(this.sthumb.id, 'slider-' + this.sthumb.id, {});
		this.dd.slider = this;
		this.dd.onDrag = this.onDrag;
		this.dd.startDrag = this.startDrag;
		
	},
	
	startDrag: function(x, y) {
		var slider = this.slider;

		var bb = slider.sbar.getBox();
		var tb = slider.sthumb.getBox();

		this.resetConstraints();
		this.setYConstraint(0, 0);
		this.setXConstraint(tb.x - bb.x + 1, bb.x + bb.width - tb.x - tb.width - 1);

		slider.pixelMax = bb.width - tb.width;

//		debugger;
	},
	onDrag: function(e) {
		var min = this.slider.minValue;
		var max = this.slider.maxValue;
		var pixelPos = parseInt(this.getEl().style.left,10);
		this.slider.setValue(min + (max - min) * pixelPos / this.slider.pixelMax);
	}
});
Ext.reg('sliderfield', Ext.form.SliderField);
// end of file
