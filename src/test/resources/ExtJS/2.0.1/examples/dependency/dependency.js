/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){
    var xt = Ext.tree;
    // seeds for the new node suffix
    var cseed = 0, oseed = 0;

    // turn on quick tips
    Ext.QuickTips.init();

    var cview = Ext.DomHelper.append('main-ct',
        {cn:[{id:'main-tb'},{id:'cbody'}]}
    );


    // create the primary toolbar
    var tb = new Ext.Toolbar('main-tb');
    tb.add({
        id:'save',
        text:'Save',
        disabled:true,
        handler:save,
        cls:'x-btn-text-icon save',
        tooltip:'Saves all components to the server'
    },'-', {
        id:'add',
        text:'Component',
        handler:addComponent,
        cls:'x-btn-text-icon add-cmp',
        tooltip:'Add a new Component to the dependency builder'
    }, {
        id:'option',
        text:'Option',
        disabled:true,
        handler:addOption,
        cls:'x-btn-text-icon add-opt',
        tooltip:'Add a new optional dependency to the selected component'
    },'-',{
        id:'remove',
        text:'Remove',
        disabled:true,
        handler:removeNode,
        cls:'x-btn-text-icon remove',
        tooltip:'Remove the selected item'
    });
    // for enabling and disabling
    var btns = tb.items.map;



    // create our layout
    var layout = new Ext.BorderLayout('main-ct', {
        west: {
            split:true,
            initialSize: 200,
            minSize: 175,
            maxSize: 400,
            titlebar: true,
            margins:{left:5,right:0,bottom:5,top:5}
        },
        center: {
            title:'Components',
            margins:{left:0,right:5,bottom:5,top:5}
        }
    }, 'main-ct');

    layout.batchAdd({
       west: {
           id: 'source-files',
           autoCreate:true,
           title:'Ext Source Files',
           autoScroll:true,
           fitToFrame:true
       },
       center : {
           el: cview,
           autoScroll:true,
           fitToFrame:true,
           toolbar: tb,
           resizeEl:'cbody'
       }
    });



    // this is the source code tree
    var stree = new xt.TreePanel('source-files', {
        animate:true,
        loader: new xt.TreeLoader({dataUrl:'dependency.php'}),
        enableDrag:true,
        containerScroll: true
    });

    new xt.TreeSorter(stree, {folderSort:true});

    var sroot = new xt.AsyncTreeNode({
        text: 'Ext JS',
        draggable:false,
        id:'source'
    });
    stree.setRootNode(sroot);
    stree.render();
    sroot.expand(false, false);


    // the component tree
    var ctree = new xt.TreePanel('cbody', {
        animate:true,
        enableDD:true,
        containerScroll: true,
        lines:false,
        rootVisible:false,
        loader: new Ext.tree.TreeLoader()
    });

    ctree.el.addKeyListener(Ext.EventObject.DELETE, removeNode);

    var croot = new xt.AsyncTreeNode({
        allowDrag:false,
        allowDrop:true,
        id:'croot',
        text:'Packages and Components',
        cls:'croot',
        loader:new Ext.tree.TreeLoader({
            dataUrl:'dep-tree.json',
            createNode: readNode
        })
    });
    ctree.setRootNode(croot);
    ctree.render();
    croot.expand();

    // some functions to determine whether is not the drop is allowed
    function hasNode(t, n){
        return (t.attributes.type == 'fileCt' && t.findChild('id', n.id)) ||
            (t.leaf === true && t.parentNode.findChild('id', n.id));
    };

    function isSourceCopy(e, n){
        var a = e.target.attributes;
        return n.getOwnerTree() == stree && !hasNode(e.target, n) &&
           ((e.point == 'append' && a.type == 'fileCt') || a.leaf === true);
    };

    function isReorder(e, n){
        return n.parentNode == e.target.parentNode && e.point != 'append';
    };

    // handle drag over and drag drop
    ctree.on('nodedragover', function(e){
        var n = e.dropNode;
        return isSourceCopy(e, n) || isReorder(e, n);
    });

    ctree.on('beforenodedrop', function(e){
        var n = e.dropNode;

        // copy node from source tree
        if(isSourceCopy(e, n)){
            var copy = new xt.TreeNode(
                Ext.apply({allowDelete:true,expanded:true}, n.attributes)
            );
            copy.loader = undefined;
            if(e.target.attributes.options){
                e.target = createOption(e.target, copy.text);
                //return false;
            }
            e.dropNode = copy;
            return true;
        }

        return isReorder(e, n);
    });

    ctree.on('contextmenu', prepareCtx);

    // track whether save is allowed
    ctree.on('append', trackSave);
    ctree.on('remove', trackSave);
    ctree.el.swallowEvent('contextmenu', true);
    ctree.el.on('keypress', function(e){
        if(e.isNavKeyPress()){
            e.stopEvent();
        }
    });
    // when the tree selection changes, enable/disable the toolbar buttons
    var sm = ctree.getSelectionModel();
    sm.on('selectionchange', function(){
        var n = sm.getSelectedNode();
        if(!n){
            btns.remove.disable();
            btns.option.disable();
            return;
        }
        var a = n.attributes;
        btns.remove.setDisabled(!a.allowDelete);
        btns.option.setDisabled(!a.cmpId);
    });



    // create the editor for the component tree
    var ge = new xt.TreeEditor(ctree, {
        allowBlank:false,
        blankText:'A name is required',
        selectOnFocus:true
    });
    
    ge.on('beforestartedit', function(){
        if(!ge.editNode.attributes.allowEdit){
            return false;
        }
    });


    // add component handler
    function addComponent(){
        var id = guid('c-');
        var text = 'Component '+(++cseed);
        var node = createComponent(id, text);
        node.expand(false, false);
        node.select();
        node.lastChild.ensureVisible();
        ge.triggerEdit(node);
    }

    function createComponent(id, text, cfiles, cdep, coptions){
        var node = new xt.TreeNode({
            text: text,
            iconCls:'cmp',
            cls:'cmp',
            type:'cmp',
            id: id,
            cmpId:id,
            allowDelete:true,
            allowEdit:true
        });
        croot.appendChild(node);

        var files = new xt.AsyncTreeNode({
            text: 'Files',
            allowDrag:false,
            allowDrop:true,
            iconCls:'folder',
            type:'fileCt',
            cmpId:id,
            allowDelete:false,
            children:cfiles||[],
            expanded:true
        });

        var dep = new xt.AsyncTreeNode({
            text: 'Dependencies',
            allowDrag:false,
            allowDrop:true,
            iconCls:'folder',
            type:'fileCt',
            cmpId:id,
            allowDelete:false,
            children:cdep||[],
            expanded:true,
            allowCopy:true
        });

        var options = new xt.AsyncTreeNode({
            text: 'Optional Dependencies',
            allowDrag:false,
            allowDrop:true,
            iconCls:'folder',
            type:'fileCt',
            options:true,
            cmpId:id,
            allowDelete:false,
            children:coptions||[],
            expanded:true,
            allowCopy:true
        });

        node.appendChild(files);
        node.appendChild(dep);
        node.appendChild(options);

        return node;
    }

    // remove handler
    function removeNode(){
        var n = sm.getSelectedNode();
        if(n && n.attributes.allowDelete){
            ctree.getSelectionModel().selectPrevious();
            n.parentNode.removeChild(n);
        }
    }


    // add option handler
    function addOption(){
        var n = sm.getSelectedNode();
        if(n){
            var node = createOption(n, 'Option'+(++oseed));
            node.select();
            ge.triggerEdit(node);
        }
    }

    function createOption(n, text){
        var cnode = ctree.getNodeById(n.attributes.cmpId);

        var node = new xt.TreeNode({
            text: text,
            cmpId:cnode.id,
            iconCls:'folder',
            type:'fileCt',
            allowDelete:true,
            allowEdit:true,
            id:guid('o-')
        });
        cnode.childNodes[2].appendChild(node);
        cnode.childNodes[2].expand(false, false);

        return node;
    }

    // semi unique ids across edits
    function guid(prefix){
        return prefix+(new Date().getTime());
    }


    function trackSave(){
        btns.save.setDisabled(!croot.hasChildNodes());
    }

    function storeChildren(cmp, n, name){
        if(n.childrenRendered){
            cmp[name] = [];
            n.eachChild(function(f){
                cmp[name].push(f.attributes);
            });
        }else{
            cmp[name] = n.attributes.children || [];
        }
    }

    // save to the server in a format usable in PHP
    function save(){
        var ch = [];
        croot.eachChild(function(c){
            var cmp = {
                text:c.text,
                id: c.id,
                options:[]
            };

            storeChildren(cmp, c.childNodes[0], 'files');
            storeChildren(cmp, c.childNodes[1], 'dep');

            var onode = c.childNodes[2];
            if(!onode.childrenRendered){
                cmp.options = onode.attributes.children || [];
            }else{
                onode.eachChild(function(o){
                    var opt = Ext.apply({}, o.attributes);
                    storeChildren(opt, o, 'children');
                    cmp.options.push(opt);
                });
            }
            ch.push(cmp);
        });

        layout.el.mask('Sending data to server...', 'x-mask-loading');
        var hide = layout.el.unmask.createDelegate(layout.el);
        Ext.lib.Ajax.request(
            'POST',
            'save-dep.php',
            {success:hide,failure:hide},
            'data='+encodeURIComponent(Ext.encode(ch))
        );
    }

    function readNode(o){
        createComponent(o.id, o.text, o.files, o.dep, o.options);
    }

    // context menus

    var ctxMenu = new Ext.menu.Menu({
        id:'copyCtx',
        items: [{
                id:'expand',
                handler:expandAll,
                cls:'expand-all',
                text:'Expand All'
            },{
                id:'collapse',
                handler:collapseAll,
                cls:'collapse-all',
                text:'Collapse All'
            },'-',{
                id:'remove',
                handler:removeNode,
                cls:'remove-mi',
                text: 'Remove Item'
        }]
    });

    function prepareCtx(node, e){
        node.select();
        ctxMenu.items.get('remove')[node.attributes.allowDelete ? 'enable' : 'disable']();
        ctxMenu.showAt(e.getXY());
    }

    function collapseAll(){
        ctxMenu.hide();
        setTimeout(function(){
            croot.eachChild(function(n){
               n.collapse(false, false);
            });
        }, 10);
    }

    function expandAll(){
        ctxMenu.hide();
        setTimeout(function(){
            croot.eachChild(function(n){
               n.expand(false, false);
            });
        }, 10);
    }
});