/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/*
 *
 */
Ext.onReady(function() {
	var win = new Ext.Window({
		title:'Slider Test Form',
		collapsible:true,
		closable:false,
		width:400,
		height:300,
		layout:'fit',
		items:{
			xtype:'form',
			id:'slider-form',
			bodyStyle:'padding:5px',
			baseCls:'x-plain',
			url:'echo.php',
			defaults:{width:200},
//			defaults:{anchor:'95%'},
			items:[{
				xtype:'textfield',
				name:'textfield1',
				anchor:'95%',
				fieldLabel:'Text Field'
			},{
				xtype:'sliderfield',
				name:'slider1',
				minValue:50,
				maxValue:100,
				anchor:'95%',
				fieldLabel:'Slider'
			}]
		},

        buttons: [{
			text: 'Save',handler:function() {
				Ext.getCmp('slider-form').getForm().submit({
					success:function(form, action) {
						alert('Success:\n' + action.response.responseText);
					},
					failure:function(form, action) {
						alert('Failure: ' + action.failureType);
					}
				});
		    }
        },{
            text: 'Cancel'
        }]
	});
	win.show();
});
// end of file
