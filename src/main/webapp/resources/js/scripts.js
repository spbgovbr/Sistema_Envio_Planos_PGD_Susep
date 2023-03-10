function irPara(ancora) {
	this.location = "#" + ancora;
}

PrimeFaces.locales['pt_BR'] = {
	    closeText: 'Fechar',
	    prevText: 'Anterior',
	    nextText: 'Próximo',
	    currentText: 'Começo',
	    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
	    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'],
	    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
	    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
	    dayNamesMin: ['D','S','T','Q','Q','S','S'],
	    weekHeader: 'Semana',
	    firstDay: 1,
	    isRTL: false,
	    showMonthAfterYear: false,
	    yearSuffix: '',
	    timeOnlyTitle: 'Só Horas',
	    timeText: 'Tempo',
	    hourText: 'Hora',
	    minuteText: 'Minuto',
	    secondText: 'Segundo',
	    currentText: 'Data Atual',
	    ampm: false,
	    month: 'Mês',
	    week: 'Semana',
	    day: 'Dia',
	    allDayText : 'Todo Dia'
};

/**
 * PrimeFaces DataTable Widget
 */
PrimeFaces.widget.DataTable = PrimeFaces.widget.DeferredWidget.extend({
    
    SORT_ORDER: {
        ASCENDING: 1,
        DESCENDING: -1,
        UNSORTED: 0
    },
    
    init: function(cfg) {
        this._super(cfg);
        
        this.thead = this.getThead(); 
        this.tbody = this.getTbody();
        
        if(this.cfg.paginator) {
            this.bindPaginator();
        }

        this.bindSortEvents();
        
        if(this.cfg.rowHover) {
            this.setupRowHover();
        }

        if(this.cfg.selectionMode) {
            this.setupSelection();
        }

        if(this.cfg.filter) {
            this.setupFiltering();
        }

        if(this.cfg.expansion) {
            this.expansionProcess = [];
            this.bindExpansionEvents();
        }

        if(this.cfg.editable) {
            this.bindEditEvents();
        }

        if(this.cfg.draggableRows) {
            this.makeRowsDraggable();
        }
        
        if(this.cfg.reflow) {
            this.initReflow();
        }
        
        this.renderDeferred();
    },
    
    _render: function() {
        if(this.cfg.scrollable) {
            this.setupScrolling();
        }

        if(this.cfg.resizableColumns) {
            this.setupResizableColumns();
        }

        if(this.cfg.draggableColumns) {
            this.setupDraggableColumns();
        }

        if(this.cfg.stickyHeader) {
            this.setupStickyHeader();
        }
    },
    
    getThead: function() {
        return $(this.jqId + '_head');
    },
    
    getTbody: function() {
        return $(this.jqId + '_data');
    },
        
    updateData: function(data, clear) {
        var empty = (clear === undefined) ? true: clear;
        
        if(empty)
            this.tbody.html(data);
        else
            this.tbody.append(data);
        
        this.postUpdateData();
    },
    
    postUpdateData: function() {
        if(this.cfg.draggableRows) {
            this.makeRowsDraggable();
        } 
        
        if(this.cfg.reflow) {
            this.initReflow();
        }
    },
 
    /**
     * @Override
     */
    refresh: function(cfg) {        
        this.columnWidthsFixed = false;
        
        this.init(cfg);
    },
    
    /**
     * Binds the change event listener and renders the paginator
     */
    bindPaginator: function() {
        var _self = this;
        this.cfg.paginator.paginate = function(newState) {
            _self.paginate(newState);
        };

        this.paginator = new PrimeFaces.widget.Paginator(this.cfg.paginator);
    },
    
    /**
     * Applies events related to sorting in a non-obstrusive way
     */
    bindSortEvents: function() {
        var $this = this;
        this.cfg.tabindex = this.cfg.tabindex||'0';
        this.sortableColumns = this.thead.find('> tr > th.ui-sortable-column');
        this.sortableColumns.attr('tabindex', this.cfg.tabindex);
        
        if(this.cfg.multiSort) {
            this.sortMeta = [];
        }

        for(var i = 0; i < this.sortableColumns.length; i++) {
            var columnHeader = this.sortableColumns.eq(i),
            sortIcon = columnHeader.children('span.ui-sortable-column-icon'),
            sortOrder = null;
    
            if(columnHeader.hasClass('ui-state-active')) {
                if(sortIcon.hasClass('ui-icon-triangle-1-n'))
                    sortOrder = this.SORT_ORDER.ASCENDING;
                else
                    sortOrder = this.SORT_ORDER.DESCENDING;
                
                if($this.cfg.multiSort) {
                    $this.addSortMeta({
                        col: columnHeader.attr('id'), 
                        order: sortOrder
                    });
                }
            }
            else {
                sortOrder = this.SORT_ORDER.UNSORTED;
            }
            
            columnHeader.data('sortorder', sortOrder);
        }
        
        this.sortableColumns.on('mouseenter.dataTable', function() {
            var column = $(this);
            
            if(!column.hasClass('ui-state-active'))
                column.addClass('ui-state-hover');
        })
        .on('mouseleave.dataTable', function() {
            var column = $(this);
            
            if(!column.hasClass('ui-state-active'))
                column.removeClass('ui-state-hover');
        })
        .on('blur.dataTable', function() {
            $(this).removeClass('ui-state-focus');
        })
        .on('focus.dataTable', function() {
            $(this).addClass('ui-state-focus');
        })
        .on('keydown.dataTable', function(e) {
            var key = e.which,
            keyCode = $.ui.keyCode;

            if((key === keyCode.ENTER||key === keyCode.NUMPAD_ENTER) && $(e.target).is(':not(:input)')) {
                $(this).trigger('click.dataTable', (e.metaKey||e.ctrlKey));
                e.preventDefault();
            }
        })
        .on('click.dataTable', function(e, metaKeyOn) {
            if(!$this.shouldSort(e, this)) {
                return;
            }

            PrimeFaces.clearSelection();
                            
            var columnHeader = $(this),
            sortOrderData = columnHeader.data('sortorder'),
            sortOrder = (sortOrderData === $this.SORT_ORDER.UNSORTED) ? $this.SORT_ORDER.ASCENDING : -1 * sortOrderData,
            metaKey = e.metaKey||e.ctrlKey||metaKeyOn;
            
            if($this.cfg.multiSort) {
                if(metaKey) {
                    $this.addSortMeta({
                        col: columnHeader.attr('id'), 
                        order: sortOrder
                    });
                    $this.sort(columnHeader, sortOrder, true);
                }
                else {                        
                    $this.sortMeta = [];
                    $this.addSortMeta({
                        col: columnHeader.attr('id'), 
                        order: sortOrder
                    });
                    $this.sort(columnHeader, sortOrder);
                }
            }
            else {
                $this.sort(columnHeader, sortOrder);
            }

        });
    },
    
    shouldSort: function(event, column) {
        if(this.isEmpty()) {
            return false;
        }
        
        var target = $(event.target);
        if(target.closest('.ui-column-customfilter', column).length) {
            return false;
        }
        
        return target.is('th,span');
    },
    
    addSortMeta: function(meta) {
        this.sortMeta = $.grep(this.sortMeta, function(value) {
            return value.col !== meta.col;
        });
        
        this.sortMeta.push(meta);
    },
      
    /**
     * Binds filter events to standard filters
     */
    setupFiltering: function() {
        var $this = this,
        filterColumns = this.thead.find('> tr > th.ui-filter-column');
        this.cfg.filterEvent = this.cfg.filterEvent||'keyup';
        this.cfg.filterDelay = this.cfg.filterDelay||300;

        filterColumns.children('.ui-column-filter').each(function() {
            var filter = $(this);

            if(filter.is('input:text')) {
                PrimeFaces.skinInput(filter);
                $this.bindTextFilter(filter);
            } 
            else {
                PrimeFaces.skinSelect(filter);
                $this.bindChangeFilter(filter);
            }
        });
    },
    
    bindTextFilter: function(filter) {
        if(this.cfg.filterEvent === 'enter')
            this.bindEnterKeyFilter(filter);
        else
            this.bindFilterEvent(filter);
    },
    
    bindChangeFilter: function(filter) {
        var $this = this;
        
        filter.change(function() {
            $this.filter();
        });
    },
    
    bindEnterKeyFilter: function(filter) {
        var $this = this;
    
        filter.bind('keydown', function(e) {
            var key = e.which,
            keyCode = $.ui.keyCode;

            if((key === keyCode.ENTER||key === keyCode.NUMPAD_ENTER)) {
                e.preventDefault();
            }
        }).bind('keyup', function(e) {
            var key = e.which,
            keyCode = $.ui.keyCode;

            if((key === keyCode.ENTER||key === keyCode.NUMPAD_ENTER)) {
                $this.filter();

                e.preventDefault();
            }
        });
    },
    
    bindFilterEvent: function(filter) {
        var $this = this;
        
        //prevent form submit on enter key
        filter.on('keydown.dataTable-blockenter', function(e) {
            var key = e.which,
            keyCode = $.ui.keyCode;

            if((key === keyCode.ENTER||key === keyCode.NUMPAD_ENTER)) {
                e.preventDefault();
            }
        })
        .on(this.cfg.filterEvent + '.dataTable', function(e) {
            if($this.filterTimeout) {
                clearTimeout($this.filterTimeout);
            }

            $this.filterTimeout = setTimeout(function() {
                $this.filter();
                $this.filterTimeout = null;
            },
            $this.cfg.filterDelay);
        });
    },
    
    setupRowHover: function() {
        var selector = '> tr.ui-widget-content';
        if(!this.cfg.selectionMode) {
            this.bindRowHover(selector);
        }
    },
    
    setupSelection: function() {
        this.selectionHolder = this.jqId + '_selection';
        this.cfg.rowSelectMode = this.cfg.rowSelectMode||'new';
        this.rowSelector = '> tr.ui-widget-content.ui-datatable-selectable';
        this.cfg.disabledTextSelection = this.cfg.disabledTextSelection === false ? false : true;

        var preselection = $(this.selectionHolder).val();
        this.selection = (preselection === "") ? [] : preselection.split(',');

        //shift key based range selection
        this.originRowIndex = 0;
        this.cursorIndex = null;

        this.bindSelectionEvents();
    },
    
    /**
     * Applies events related to selection in a non-obstrusive way
     */
    bindSelectionEvents: function() {
        if(this.cfg.selectionMode === 'radio') {
            this.bindRadioEvents();
        }
        else if(this.cfg.selectionMode === 'checkbox') {
            this.bindCheckboxEvents();
            this.updateHeaderCheckbox();
            
            if(this.cfg.rowSelectMode !== 'checkbox') {
                this.bindRowEvents();
            }
        }
        else {
            this.bindRowEvents();
        }
    },
    
    bindRowEvents: function() {
        var $this = this;

        this.bindRowHover(this.rowSelector);

        this.tbody.off('click.dataTable', this.rowSelector).on('click.dataTable', this.rowSelector, null, function(e) {
            $this.onRowClick(e, this);
        });
        
        //double click
        if(this.hasBehavior('rowDblselect')) {
            this.tbody.off('dblclick.dataTable', this.rowSelector).on('dblclick.dataTable', this.rowSelector, null, function(e) {
                $this.onRowDblclick(e, $(this));
            });
        };
    },
    
    bindRowHover: function(selector) {
        this.tbody.off('mouseenter.dataTable mouseleave.dataTable', selector)
                    .on('mouseenter.dataTable', selector, null, function() {
                        var element = $(this);

                        if(!element.hasClass('ui-state-highlight')) {
                            element.addClass('ui-state-hover');
                        }
                    })
                    .on('mouseleave.dataTable', selector, null, function() {
                        var element = $(this);

                        if(!element.hasClass('ui-state-highlight')) {
                            element.removeClass('ui-state-hover');
                        }
                    });
    },
    
    bindRadioEvents: function() {
        var $this = this,
        radioInputSelector = '> tr.ui-widget-content:not(.ui-datatable-empty-message) > td.ui-selection-column :radio';
        
        if(this.cfg.nativeElements) {            
            this.tbody.off('click.dataTable', radioInputSelector).on('click.dataTable', radioInputSelector, null, function(e) {
                var radioButton = $(this);

                if(!radioButton.prop('checked'))
                    $this.selectRowWithRadio(radioButton);
            });
        }
        else {
            var radioSelector = '> tr.ui-widget-content:not(.ui-datatable-empty-message) > td.ui-selection-column .ui-radiobutton .ui-radiobutton-box';
            this.tbody.off('click.dataTable mouseover.dataTable mouseout.dataTable', radioSelector)
                .on('mouseover.dataTable', radioSelector, null, function() {
                    var radio = $(this);
                    if(!radio.hasClass('ui-state-disabled')&&!radio.hasClass('ui-state-active')) {
                        radio.addClass('ui-state-hover');
                    }
                })
                .on('mouseout.dataTable', radioSelector, null, function() {
                    var radio = $(this);
                    radio.removeClass('ui-state-hover');
                })
                .on('click.dataTable', radioSelector, null, function() {
                    var radio = $(this),
                    checked = radio.hasClass('ui-state-active'),
                    disabled = radio.hasClass('ui-state-disabled');

                    if(!disabled && !checked) {
                        $this.selectRowWithRadio(radio);
                    }
                });
        }
                       
        //keyboard support
        this.tbody.off('focus.dataTable blur.dataTable change.dataTable', radioInputSelector)
            .on('focus.dataTable', radioInputSelector, null, function() {
                var input = $(this),
                box = input.parent().next();

                if(input.prop('checked')) {
                    box.removeClass('ui-state-active');
                }

                box.addClass('ui-state-focus');
            })
            .on('blur.dataTable', radioInputSelector, null, function() {
                var input = $(this),
                box = input.parent().next();

                if(input.prop('checked')) {
                    box.addClass('ui-state-active');
                }

                box.removeClass('ui-state-focus');
            })
            .on('change.dataTable', radioInputSelector, null, function() {
                var currentInput = $this.tbody.find(radioInputSelector).filter(':checked'),
                currentRadio = currentInput.parent().next();

                $this.selectRowWithRadio(currentRadio);
            });
                      
    },
    
    bindCheckboxEvents: function() {
        var $this = this,
        checkboxInputSelector = '> tr.ui-widget-content.ui-datatable-selectable > td.ui-selection-column :checkbox';

        if(this.cfg.nativeElements) {
            this.checkAllToggler = this.thead.find('> tr > th.ui-selection-column > :checkbox');
            
            this.checkAllToggler.on('click', function() {
                $this.toggleCheckAll();
            });
            
            this.tbody.off('click.dataTable', checkboxInputSelector).on('click.dataTable', checkboxInputSelector, null, function(e) {
                var checkbox = $(this);

                if(checkbox.prop('checked'))
                    $this.selectRowWithCheckbox(checkbox);
                else                      
                    $this.unselectRowWithCheckbox(checkbox);
            });
        }
        else {
            this.checkAllToggler = this.thead.find('> tr > th.ui-selection-column > .ui-chkbox.ui-chkbox-all > .ui-chkbox-box');
            
            this.checkAllToggler.on('mouseover', function() {
                var box = $(this);
                if(!box.hasClass('ui-state-disabled')&&!box.hasClass('ui-state-active')) {
                    box.addClass('ui-state-hover');
                }
            })
            .on('mouseout', function() {
                $(this).removeClass('ui-state-hover');
            })
            .on('click', function() {
                var box = $(this);
                if(!box.hasClass('ui-state-disabled')) {
                    $this.toggleCheckAll();
                }
            });
            
            var checkboxSelector = '> tr.ui-widget-content.ui-datatable-selectable > td.ui-selection-column .ui-chkbox .ui-chkbox-box';
            this.tbody.off('mouseover.dataTable mouseover.dataTable click.dataTable', checkboxSelector)
                        .on('mouseover.dataTable', checkboxSelector, null, function() {
                            var box = $(this);
                            if(!box.hasClass('ui-state-active')) {
                                box.addClass('ui-state-hover');
                            }
                        })
                        .on('mouseout.dataTable', checkboxSelector, null, function() {
                            $(this).removeClass('ui-state-hover');
                        })
                        .on('click.dataTable', checkboxSelector, null, function() {
                            var checkbox = $(this),
                            checked = checkbox.hasClass('ui-state-active');

                            if(checked)
                                $this.unselectRowWithCheckbox(checkbox);
                            else                      
                                $this.selectRowWithCheckbox(checkbox);
                        });
        }
        
        //keyboard support
        this.tbody.off('focus.dataTable blur.dataTable keydown.dataTable keyup.dataTable', checkboxInputSelector)
                    .on('focus.dataTable', checkboxInputSelector, null, function() {
                        var input = $(this),
                        box = input.parent().next();

                        if(input.prop('checked')) {
                            box.removeClass('ui-state-active');
                        }

                        box.addClass('ui-state-focus');
                    })
                    .on('blur.dataTable', checkboxInputSelector, null, function() {
                        var input = $(this),
                        box = input.parent().next();

                        if(input.prop('checked')) {
                            box.addClass('ui-state-active');
                        }

                        box.removeClass('ui-state-focus');
                    })
                    .on('keydown.dataTable', checkboxInputSelector, null, function(e) {
                        var keyCode = $.ui.keyCode;
                        if(e.which === keyCode.SPACE) {
                            e.preventDefault();
                        }
                    })
                    .on('keyup.dataTable', checkboxInputSelector, null, function(e) {
                        var keyCode = $.ui.keyCode;

                        if(e.which === keyCode.SPACE) {
                            var input = $(this),
                            box = input.parent().next();

                            if(input.prop('checked')) {
                                $this.unselectRowWithCheckbox(box);
                            } 
                            else {                        
                                $this.selectRowWithCheckbox(box);
                            }

                            e.preventDefault();
                        }
                    });
    },
    
    /**
     * Applies events related to row expansion in a non-obstrusive way
     */
    bindExpansionEvents: function() {
        var $this = this,
        togglerSelector = '> tr > td > div.ui-row-toggler';
        
        this.tbody.off('click.datatable-expansion', togglerSelector)
                    .on('click.datatable-expansion', togglerSelector, null, function() {
                        $this.toggleExpansion($(this));
                    });
    },
    
    initReflow: function() {
        var headerColumns = this.thead.find('> tr > th');
        
        for(var i = 0; i < headerColumns.length; i++) {
            var headerColumn = headerColumns.eq(i),
            title = headerColumn.children('.ui-column-title').text();
            this.tbody.find('> tr > td:nth-child(' + (i + 1) + ')').prepend('<span class="ui-column-title">' + title + '</span>');
        }
    },
    
    setupScrolling: function() {
        this.scrollHeader = this.jq.children('.ui-datatable-scrollable-header');
        this.scrollBody = this.jq.children('.ui-datatable-scrollable-body');
        this.scrollFooter = this.jq.children('.ui-datatable-scrollable-footer');
        this.scrollStateHolder = $(this.jqId + '_scrollState');
        this.scrollHeaderBox = this.scrollHeader.children('div.ui-datatable-scrollable-header-box');
        this.scrollFooterBox = this.scrollFooter.children('div.ui-datatable-scrollable-footer-box');
        this.headerTable = this.scrollHeaderBox.children('table');
        this.bodyTable = this.scrollBody.children('table');
        this.footerTable = this.scrollFooter.children('table');
        this.footerCols = this.scrollFooter.find('> .ui-datatable-scrollable-footer-box > table > tfoot > tr > td');
        this.percentageScrollHeight = this.cfg.scrollHeight && (this.cfg.scrollHeight.indexOf('%') !== -1);
        this.percentageScrollWidth = this.cfg.scrollWidth && (this.cfg.scrollWidth.indexOf('%') !== -1);
        var $this = this,
        scrollBarWidth = this.getScrollbarWidth() + 'px';
        
        if(this.cfg.scrollHeight) {
            if(this.percentageScrollHeight) {
                this.adjustScrollHeight();
            }
            
            if(this.hasVerticalOverflow()) {
                this.scrollHeaderBox.css('margin-right', scrollBarWidth);
                this.scrollFooterBox.css('margin-right', scrollBarWidth);
            }
        }
                
        this.fixColumnWidths();
                
        if(this.cfg.scrollWidth) {
            if(this.percentageScrollWidth)
                this.adjustScrollWidth();
            else
                this.setScrollWidth(parseInt(this.cfg.scrollWidth));
        }
        
        this.cloneHead();
              
        this.restoreScrollState();

        if(this.cfg.liveScroll) {
            this.scrollOffset = 0;
            this.cfg.liveScrollBuffer = (100 - this.cfg.liveScrollBuffer) / 100;
            this.shouldLiveScroll = true;       
            this.loadingLiveScroll = false;
            this.allLoadedLiveScroll = $this.cfg.scrollStep >= $this.cfg.scrollLimit;      
        }
        
        this.scrollBody.on('scroll.dataTable', function() {
            var scrollLeft = $this.scrollBody.scrollLeft();
            $this.scrollHeaderBox.css('margin-left', -scrollLeft);
            $this.scrollFooterBox.css('margin-left', -scrollLeft);

            if($this.shouldLiveScroll) {
                var scrollTop = this.scrollTop,
                scrollHeight = this.scrollHeight,
                viewportHeight = this.clientHeight;

                if((scrollTop >= ((scrollHeight * $this.cfg.liveScrollBuffer) - (viewportHeight))) && $this.shouldLoadLiveScroll()) {
                    $this.loadLiveRows();
                }
            }
            
            $this.saveScrollState();
        });
        
        this.scrollHeader.on('scroll.dataTable', function() {
            $this.scrollHeader.scrollLeft(0);
        });
        
        this.scrollFooter.on('scroll.dataTable', function() {
            $this.scrollFooter.scrollLeft(0);
        });
        
        var resizeNS = 'resize.' + this.id;
        $(window).unbind(resizeNS).bind(resizeNS, function() {
            if($this.jq.is(':visible')) {
                if($this.percentageScrollHeight)
                    $this.adjustScrollHeight();
                
                if($this.percentageScrollWidth)
                    $this.adjustScrollWidth();
            }
        });
    },
    
    shouldLoadLiveScroll: function() {
        return (!this.loadingLiveScroll && !this.allLoadedLiveScroll);
    },
            
    cloneHead: function() {
        this.theadClone = this.thead.clone();
        this.theadClone.find('th').each(function() {
            var header = $(this);
            header.attr('id', header.attr('id') + '_clone');
            $(this).children().not('.ui-column-title').remove();
        });
        this.theadClone.removeAttr('id').addClass('ui-datatable-scrollable-theadclone').height(0).prependTo(this.bodyTable);
        
        //align horizontal scroller on keyboard tab
        if(this.cfg.scrollWidth) {
            this.sortableColumns.attr('tabindex', "-1").off('blur.dataTable focus.dataTable keydown.dataTable');
            
            var clonedSortableColumns = this.theadClone.find('> tr > th.ui-sortable-column');
            clonedSortableColumns.each(function() {
                $(this).data('original', $(this).attr('id').split('_clone')[0]);
            });
                        
            clonedSortableColumns.on('blur.dataTable', function() {
                $(PrimeFaces.escapeClientId($(this).data('original'))).removeClass('ui-state-focus');
            })
            .on('focus.dataTable', function() {
                $(PrimeFaces.escapeClientId($(this).data('original'))).addClass('ui-state-focus');
            })
            .on('keydown.dataTable', function(e) {
                var key = e.which,
                keyCode = $.ui.keyCode;

                if((key === keyCode.ENTER||key === keyCode.NUMPAD_ENTER) && $(e.target).is(':not(:input)')) {
                    $(PrimeFaces.escapeClientId($(this).data('original'))).trigger('click.dataTable', (e.metaKey||e.ctrlKey));
                    e.preventDefault();
                }
            });
        }
    },
            
    adjustScrollHeight: function() {
        var relativeHeight = this.jq.parent().innerHeight() * (parseInt(this.cfg.scrollHeight) / 100),
        tableHeaderHeight = this.jq.children('.ui-datatable-header').outerHeight(true),
        tableFooterHeight = this.jq.children('.ui-datatable-footer').outerHeight(true),
        scrollersHeight = (this.scrollHeader.outerHeight(true) + this.scrollFooter.outerHeight(true)),
        paginatorsHeight = this.paginator ? this.paginator.getContainerHeight(true) : 0,
        height = (relativeHeight - (scrollersHeight + paginatorsHeight + tableHeaderHeight + tableFooterHeight));
        
        this.scrollBody.height(height);
    },
            
    adjustScrollWidth: function() {
        var width = parseInt((this.jq.parent().innerWidth() * (parseInt(this.cfg.scrollWidth) / 100)));
        this.setScrollWidth(width);
    },
    
    setOuterWidth: function(element, width) {
        var diff = element.outerWidth() - element.width();
        element.width(width - diff);
    },
            
    setScrollWidth: function(width) {
        var $this = this;
        this.jq.children('.ui-widget-header').each(function() {
            $this.setOuterWidth($(this), width);
        });
        this.scrollHeader.width(width);
        this.scrollBody.css('margin-right', 0).width(width);
        this.scrollFooter.width(width);
    },
    
    alignScrollBody: function() {
        var marginRight = this.hasVerticalOverflow() ? this.getScrollbarWidth() + 'px' : '0px';
        
        this.scrollHeaderBox.css('margin-right', marginRight);
        this.scrollFooterBox.css('margin-right', marginRight);
    },
    
    getScrollbarWidth: function() {
        if(!this.scrollbarWidth) {
            this.scrollbarWidth = $.browser.webkit ? '15' : PrimeFaces.calculateScrollbarWidth();
        }
        
        return this.scrollbarWidth;
    },
        
    hasVerticalOverflow: function() {
        return (this.cfg.scrollHeight && this.bodyTable.outerHeight() > this.scrollBody.outerHeight())
    },
    
    restoreScrollState: function() {
        var scrollState = this.scrollStateHolder.val(),
        scrollValues = scrollState.split(',');

        this.scrollBody.scrollLeft(scrollValues[0]);
        this.scrollBody.scrollTop(scrollValues[1]);
    },
    
    saveScrollState: function() {
        var scrollState = this.scrollBody.scrollLeft() + ',' + this.scrollBody.scrollTop();
        
        this.scrollStateHolder.val(scrollState);
    },
    
    clearScrollState: function() {
        this.scrollStateHolder.val('0,0');
    },
    
    fixColumnWidths: function() {
        var $this = this;
        
        if(!this.columnWidthsFixed) {
            if(PrimeFaces.isIE(7)) {
                this.bodyTable.css('width', 'auto');
            }
            
            if(this.cfg.scrollable) {
                this.scrollHeader.find('> .ui-datatable-scrollable-header-box > table > thead > tr > th').each(function() {
                    var headerCol = $(this),
                    colIndex = headerCol.index(),
                    width = headerCol.width();

                    headerCol.width(width);

                    if($this.footerCols.length > 0) {
                        var footerCol = $this.footerCols.eq(colIndex);
                        footerCol.width(width);
                    }
                });
            }
            else {
                this.jq.find('> .ui-datatable-tablewrapper > table > thead > tr > th').each(function() {
                    var col = $(this);
                    col.width(col.width());
                });
            }

            this.columnWidthsFixed = true;
        }
    },
    
    /**
     * Loads rows on-the-fly when scrolling live
     */
    loadLiveRows: function() {
        if(this.liveScrollActive) {
            return;
        }
        
        this.liveScrollActive = true;
        this.scrollOffset += this.cfg.scrollStep;

        //Disable scroll if there is no more data left
        if(this.scrollOffset === this.cfg.scrollLimit) {
            this.shouldLiveScroll = false;
        }
        
        var $this = this,
        options = {
            source: this.id,
            process: this.id,
            update: this.id,
            formId: this.cfg.formId,
            params: [{name: this.id + '_scrolling', value: true},
                            {name: this.id + '_skipChildren', value: true},
                            {name: this.id + '_scrollOffset', value: this.scrollOffset},
                            {name: this.id + '_encodeFeature', value: true}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                    widget: $this,
                    handle: function(content) {
                        //insert new rows
                        this.updateData(content, false);
 
                        this.liveScrollActive = false;
                    }
                });

                return true;
            },
            oncomplete: function() {
                $this.loadingLiveScroll = false;
                $this.allLoadedLiveScroll = ($this.scrollOffset + $this.cfg.scrollStep) >= $this.cfg.scrollLimit;
            }
        };

        PrimeFaces.ajax.Request.handle(options);
    },
    
    /**
     * Ajax pagination
     */
    paginate: function(newState) {
        var $this = this,
        options = {
            source: this.id,
            update: this.id,
            process: this.id,
            formId: this.cfg.formId,
            params: [{name: this.id + '_pagination', value: true},
                    {name: this.id + '_first', value: newState.first},
                    {name: this.id + '_rows', value: newState.rows},
                    {name: this.id + '_encodeFeature', value: true}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                        widget: $this,
                        handle: function(content) {
                            this.updateData(content);
                            
                            if(this.checkAllToggler) {
                                this.updateHeaderCheckbox();
                            }
                            
                            if(this.cfg.scrollable) {
                                this.alignScrollBody();
                            }
                        }
                    });

                return true;
            },
            oncomplete: function() {
                $this.paginator.cfg.page = newState.page;
                $this.paginator.updateUI();
            }
            
        };

        if(this.hasBehavior('page')) {
            var pageBehavior = this.cfg.behaviors['page'];

            pageBehavior.call(this, options);
        } 
        else {
            PrimeFaces.ajax.Request.handle(options); 
        }
    },
    
    /**
     * Ajax sort
     */
    sort: function(columnHeader, order, multi) {  
        var $this = this,
        options = {
            source: this.id,
            update: this.id,
            process: this.id,
            params: [{name: this.id + '_sorting', value: true},
                     {name: this.id + '_skipChildren', value: true},
                     {name: this.id + '_encodeFeature', value: true}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                        widget: $this,
                        handle: function(content) {
                            this.updateData(content);
                            
                            var paginator = $this.getPaginator();
                            if(paginator) {
                                paginator.setPage(0, true);
                            }
                            
                            if(!multi) {
                                this.sortableColumns.filter('.ui-state-active').data('sortorder', this.SORT_ORDER.UNSORTED).removeClass('ui-state-active')
                                            .find('.ui-sortable-column-icon').removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');
                            }

                            columnHeader.data('sortorder', order).removeClass('ui-state-hover').addClass('ui-state-active');
                            var sortIcon = columnHeader.find('.ui-sortable-column-icon');
                            if(order === this.SORT_ORDER.DESCENDING) {
                                sortIcon.removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
                            } else if(order === this.SORT_ORDER.ASCENDING) {
                                sortIcon.removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
                            }
                        }
                    });

                return true;
            },
            oncomplete: function(xhr, status, args) {
                var paginator = $this.getPaginator();             
                if(paginator && args && paginator.cfg.rowCount !== args.totalRecords) {
                    paginator.setTotalRecords(args.totalRecords);
                }
            }
        };
        
        if(multi) {
            options.params.push({name: this.id + '_multiSorting', value: true});
            options.params.push({name: this.id + '_sortKey', value: $this.joinSortMetaOption('col')});
            options.params.push({name: this.id + '_sortDir', value: $this.joinSortMetaOption('order')});
        }
        else {
            options.params.push({name: this.id + '_sortKey', value: columnHeader.attr('id')});
            options.params.push({name: this.id + '_sortDir', value: order});
        }

        if(this.hasBehavior('sort')) {
            var sortBehavior = this.cfg.behaviors['sort'];

            sortBehavior.call(this, options);
        } 
        else {
            PrimeFaces.ajax.Request.handle(options); 
        }
    },
    
    joinSortMetaOption: function(option) {
        var value = '';
        
        for(var i = 0; i < this.sortMeta.length; i++) {
            value += this.sortMeta[i][option];
            
            if(i !== (this.sortMeta.length - 1)) {
                value += ',';
            }
        }
        
        return value;
    },
        
    /**
     * Ajax filter
     */
    filter: function() {
        var $this = this,
        options = {
            source: this.id,
            update: this.id,
            process: this.id,
            formId: this.cfg.formId,
            params: [{name: this.id + '_filtering', value: true},
                     {name: this.id + '_encodeFeature', value: true}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                        widget: $this,
                        handle: function(content) {
                            this.updateData(content);

                            if(this.cfg.scrollable) {
                                this.alignScrollBody();
                            }

                            if(this.isCheckboxSelectionEnabled()) {
                                this.updateHeaderCheckbox();
                            }
                        }
                    });

                return true;
            },
            oncomplete: function(xhr, status, args) {
                var paginator = $this.getPaginator();
                if(paginator) {
                    paginator.setTotalRecords(args.totalRecords);
                }
            }
        };

        if(this.hasBehavior('filter')) {
            var filterBehavior = this.cfg.behaviors['filter'];

            filterBehavior.call(this, options);
        } 
        else {
            PrimeFaces.ajax.AjaxRequest(options); 
        }
    },
    
    onRowClick: function(event, rowElement, silent) {    
        //Check if rowclick triggered this event not a clickable element in row content
        if($(event.target).is('td:not(.ui-column-unselectable),span:not(.ui-c)')) {
            var row = $(rowElement),
            selected = row.hasClass('ui-state-highlight'),
            metaKey = event.metaKey||event.ctrlKey,
            shiftKey = event.shiftKey;

            //unselect a selected row if metakey is on
            if(selected && metaKey) {
                this.unselectRow(row, silent);
            }
            else {
                //unselect previous selection if this is single selection or multiple one with no keys
                if(this.isSingleSelection() || (this.isMultipleSelection() && event && !metaKey && !shiftKey && this.cfg.rowSelectMode === 'new' )) {
                    this.unselectAllRows();
                }
                
                //range selection with shift key
                if(this.isMultipleSelection() && event && event.shiftKey) {                    
                    this.selectRowsInRange(row);
                }
                //select current row
                else {
                    this.originRowIndex = row.index();
                    this.cursorIndex = null;
                    this.selectRow(row, silent);
                }
            } 

            if(this.cfg.disabledTextSelection) {
                PrimeFaces.clearSelection();
            }
        }
    },
    
    onRowDblclick: function(event, row) {
        if(this.cfg.disabledTextSelection) {
            PrimeFaces.clearSelection();
        }
        
        //Check if rowclick triggered this event not a clickable element in row content
        if($(event.target).is('td,span:not(.ui-c)')) {
            var rowMeta = this.getRowMeta(row);

            this.fireRowSelectEvent(rowMeta.key, 'rowDblselect');
        }
    },
    
    onRowRightClick: function(event, rowElement, cmSelMode) {
        var row = $(rowElement),
        rowMeta = this.getRowMeta(row),
        selected = row.hasClass('ui-state-highlight');

        if(cmSelMode === 'single' || !selected) {
            this.unselectAllRows();
        }

        this.selectRow(row, true);

        this.fireRowSelectEvent(rowMeta.key, 'contextMenu');

        if(this.cfg.disabledTextSelection) {
            PrimeFaces.clearSelection();
        }
    },
        
    /**
     * @param r {Row Index || Row Element}
     */
    findRow: function(r) {
        var row = r;

        if(PrimeFaces.isNumber(r)) {
            row = this.tbody.children('tr:eq(' + r + ')');
        }

        return row;
    },
    
    selectRowsInRange: function(row) {
        var rows = this.tbody.children(),
        rowMeta = this.getRowMeta(row),
        $this = this;
       
        //unselect previously selected rows with shift
        if(this.cursorIndex !== null) {
            var oldCursorIndex = this.cursorIndex,
            rowsToUnselect = oldCursorIndex > this.originRowIndex ? rows.slice(this.originRowIndex, oldCursorIndex + 1) : rows.slice(oldCursorIndex, this.originRowIndex + 1);

            rowsToUnselect.each(function(i, item) {
                $this.unselectRow($(item), true);
            });
        }

        //select rows between cursor and origin
        this.cursorIndex = row.index();

        var rowsToSelect = this.cursorIndex > this.originRowIndex ? rows.slice(this.originRowIndex, this.cursorIndex + 1) : rows.slice(this.cursorIndex, this.originRowIndex + 1);

        rowsToSelect.each(function(i, item) {
            $this.selectRow($(item), true);
        });
        
        this.fireRowSelectEvent(rowMeta.key, 'rowSelect');
    },
    
    selectRow: function(r, silent) {
        var row = this.findRow(r),
        rowMeta = this.getRowMeta(row);

        this.highlightRow(row);

        if(this.isCheckboxSelectionEnabled()) {
            if(this.cfg.nativeElements)
                row.children('td.ui-selection-column').find(':checkbox').prop('checked', true);
            else
                this.selectCheckbox(row.children('td.ui-selection-column').find('> div.ui-chkbox > div.ui-chkbox-box'));
            
            this.updateHeaderCheckbox();
        }
        
        this.addSelection(rowMeta.key);

        this.writeSelections();

        if(!silent) {
            this.fireRowSelectEvent(rowMeta.key, 'rowSelect');
        }
    },
    
    unselectRow: function(r, silent) {
        var row = this.findRow(r),
        rowMeta = this.getRowMeta(row);

        this.unhighlightRow(row);
        
        if(this.isCheckboxSelectionEnabled()) {
            if(this.cfg.nativeElements)
                row.children('td.ui-selection-column').find(':checkbox').prop('checked', false);
            else
                this.unselectCheckbox(row.children('td.ui-selection-column').find('> div.ui-chkbox > div.ui-chkbox-box'));
            
            this.updateHeaderCheckbox();
        }

        this.removeSelection(rowMeta.key);

        this.writeSelections();

        if(!silent) {
            this.fireRowUnselectEvent(rowMeta.key, "rowUnselect");
        }
    },
    
    /*
     * Highlights row as selected
     */
    highlightRow: function(row) {
        row.removeClass('ui-state-hover').addClass('ui-state-highlight').attr('aria-selected', true);
    },
    
    /*
     * Clears selected visuals
     */
    unhighlightRow: function(row) {
        row.removeClass('ui-state-highlight').attr('aria-selected', false);
    },
    
    /**
     * Sends a rowSelectEvent on server side to invoke a rowSelectListener if defined
     */
    fireRowSelectEvent: function(rowKey, behaviorEvent) {
        if(this.cfg.behaviors) {
            var selectBehavior = this.cfg.behaviors[behaviorEvent];

            if(selectBehavior) {
                var ext = {
                        params: [{name: this.id + '_instantSelectedRowKey', value: rowKey}
                    ]
                };

                selectBehavior.call(this, ext);
            }
        }
    },
    
    /**
     * Sends a rowUnselectEvent on server side to invoke a rowUnselectListener if defined
     */
    fireRowUnselectEvent: function(rowKey, behaviorEvent) {
        if(this.cfg.behaviors) {
            var unselectBehavior = this.cfg.behaviors[behaviorEvent];

            if(unselectBehavior) {
                var ext = {
                    params: [
                    {
                        name: this.id + '_instantUnselectedRowKey', 
                        value: rowKey
                    }
                    ]
                };

                unselectBehavior.call(this, ext);
            }
        }
    },
    
    /**
     * Selects the corresping row of a radio based column selection
     */
    selectRowWithRadio: function(radio) {
        var row = radio.closest('tr'),
        rowMeta = this.getRowMeta(row);

        //clean selection       
        this.unselectAllRows();
               
        //select current
        if(!this.cfg.nativeElements) {
            this.selectRadio(radio);
        }
        
        this.highlightRow(row);
        this.addSelection(rowMeta.key);
        this.writeSelections();
        this.fireRowSelectEvent(rowMeta.key, 'rowSelectRadio');
    },
    
    /**
     * Selects the corresponding row of a checkbox based column selection
     */
    selectRowWithCheckbox: function(checkbox, silent) {
        var row = checkbox.closest('tr'),
        rowMeta = this.getRowMeta(row);

        this.highlightRow(row);
        
        if(!this.cfg.nativeElements) {
            this.selectCheckbox(checkbox);
        }

        this.addSelection(rowMeta.key);

        this.writeSelections();

        if(!silent) {
            this.updateHeaderCheckbox();
            this.fireRowSelectEvent(rowMeta.key, "rowSelectCheckbox");
        }
    },
    
    /**
     * Unselects the corresponding row of a checkbox based column selection
     */
    unselectRowWithCheckbox: function(checkbox, silent) {
        var row = checkbox.closest('tr'),
        rowMeta = this.getRowMeta(row);

        this.unhighlightRow(row);
        
        if(!this.cfg.nativeElements) {
            this.unselectCheckbox(checkbox);
        }

        this.removeSelection(rowMeta.key);
        
        this.uncheckHeaderCheckbox();

        this.writeSelections();

        if(!silent) {
            this.fireRowUnselectEvent(rowMeta.key, "rowUnselectCheckbox");
        }
    },
    
    unselectAllRows: function() {
        var selectedRows = this.tbody.children('tr.ui-state-highlight'),
        checkboxSelectionEnabled = this.isCheckboxSelectionEnabled(),
        radioSelectionEnabled = this.isRadioSelectionEnabled();

        for(var i = 0; i < selectedRows.length; i++) {
            var row = selectedRows.eq(i);
            
            this.unhighlightRow(row);
            
            if(checkboxSelectionEnabled) {
                if(this.cfg.nativeElements)
                    row.children('td.ui-selection-column').find(':checkbox').prop('checked', false);
                else
                    this.unselectCheckbox(row.children('td.ui-selection-column').find('> div.ui-chkbox > div.ui-chkbox-box'));
            }
            else if(radioSelectionEnabled) {
                if(this.cfg.nativeElements)
                    row.children('td.ui-selection-column').find(':radio').prop('checked', false);
                else
                    this.unselectRadio(row.children('td.ui-selection-column').find('> div.ui-radiobutton > div.ui-radiobutton-box'));
            }
        }
        
        if(checkboxSelectionEnabled) {
            this.uncheckHeaderCheckbox();
        }
        
        this.selection = [];
        this.writeSelections();
    },
            
    selectAllRowsOnPage: function() {
        var rows = this.tbody.children('tr');
        for(var i = 0; i < rows.length; i++) {
            var row = rows.eq(i);
            this.selectRow(row, true);
        }
    },
            
    unselectAllRowsOnPage: function() {
        var rows = this.tbody.children('tr');
        for(var i = 0; i < rows.length; i++) {
            var row = rows.eq(i);
            this.unselectRow(row, true);
        }
    },
            
    selectAllRows: function() {
        this.selectAllRowsOnPage();
        this.selection = new Array('@all');
        this.writeSelections();
    },
    
    /**
     * Toggles all rows with checkbox
     */
    toggleCheckAll: function() {
        if(this.cfg.nativeElements) {
            var checkboxes = this.tbody.find('> tr.ui-datatable-selectable > td.ui-selection-column > :checkbox'),
            checked = this.checkAllToggler.prop('checked'),
            $this = this;
    
            checkboxes.each(function() {
                if(checked) {
                    var checkbox = $(this);
                    checkbox.prop('checked', true);
                    $this.selectRowWithCheckbox(checkbox, true);
                }
                else {
                    var checkbox = $(this);
                    checkbox.prop('checked', false);
                    $this.unselectRowWithCheckbox(checkbox, true);
                }
            });
        }
        else {
            var checkboxes = this.tbody.find('> tr.ui-datatable-selectable > td.ui-selection-column .ui-chkbox-box'),
            checked = this.checkAllToggler.hasClass('ui-state-active'),
            $this = this;

            if(checked) {
                this.checkAllToggler.removeClass('ui-state-active').children('span.ui-chkbox-icon').addClass('ui-icon-blank').removeClass('ui-icon-check');

                checkboxes.each(function() {
                    $this.unselectRowWithCheckbox($(this), true);
                });
            } 
            else {
                this.checkAllToggler.addClass('ui-state-active').children('span.ui-chkbox-icon').removeClass('ui-icon-blank').addClass('ui-icon-check');

                checkboxes.each(function() {
                    $this.selectRowWithCheckbox($(this), true);
                });
            }
        }
                
        //save state
        this.writeSelections();

        //fire toggleSelect event
        if(this.cfg.behaviors) {
            var toggleSelectBehavior = this.cfg.behaviors['toggleSelect'];

            if(toggleSelectBehavior) {
                var ext = {
                        params: [{name: this.id + '_checked', value: !checked}
                    ]
                };
                
                toggleSelectBehavior.call(this, ext);
            }
        }
    },
    
    selectCheckbox: function(checkbox) {
        if(!checkbox.hasClass('ui-state-focus')) {
            checkbox.addClass('ui-state-active');
        }
        checkbox.children('span.ui-chkbox-icon:first').removeClass('ui-icon-blank').addClass(' ui-icon-check');
        checkbox.prev().children('input').prop('checked', true);
    },
    
    unselectCheckbox: function(checkbox) {
        checkbox.removeClass('ui-state-active');
        checkbox.children('span.ui-chkbox-icon:first').addClass('ui-icon-blank').removeClass('ui-icon-check');
        checkbox.prev().children('input').prop('checked', false); 
    },
    
    selectRadio: function(radio){
        radio.removeClass('ui-state-hover');
        if(!radio.hasClass('ui-state-focus')) {
            radio.addClass('ui-state-active');
        }
        radio.children('.ui-radiobutton-icon').addClass('ui-icon-bullet').removeClass('ui-icon-blank');
        radio.prev().children('input').prop('checked', true);
    },
            
    unselectRadio: function(radio){
        radio.removeClass('ui-state-active').children('.ui-radiobutton-icon').addClass('ui-icon-blank').removeClass('ui-icon-bullet');
        radio.prev().children('input').prop('checked', false); 
    },
    
    /**
     * Expands a row to display detail content
     */
    toggleExpansion: function(toggler) {
        var row = toggler.closest('tr'),
        rowIndex = this.getRowMeta(row).index,
        expanded = toggler.hasClass('ui-icon-circle-triangle-s'),
        $this = this;

        //Run toggle expansion if row is not being toggled already to prevent conflicts
        if($.inArray(rowIndex, this.expansionProcess) === -1) {
            this.expansionProcess.push(rowIndex);
            
            if(expanded) {
                toggler.addClass('ui-icon-circle-triangle-e').removeClass('ui-icon-circle-triangle-s');
                this.collapseRow(row);
                $this.expansionProcess = $.grep($this.expansionProcess, function(r) {
                    return (r !== rowIndex);
                });
                this.fireRowCollapseEvent(row);
            }
            else {
                if(this.cfg.rowExpandMode === 'single') {
                    this.collapseAllRows();
                }
                
                toggler.addClass('ui-icon-circle-triangle-s').removeClass('ui-icon-circle-triangle-e');

                this.loadExpandedRowContent(row);
            }
        }
    },
    
    loadExpandedRowContent: function(row) {
        var $this = this,
        rowIndex = this.getRowMeta(row).index,
        options = {
            source: this.id,
            process: this.id,
            update: this.id,
            formId: this.cfg.formId,
            params: [{name: this.id + '_rowExpansion', value: true},
                     {name: this.id + '_expandedRowIndex', value: rowIndex},
                     {name: this.id + '_encodeFeature', value: true},
                     {name: this.id + '_skipChildren', value: true}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                        widget: $this,
                        handle: function(content) {
                            if(content && $.trim(content).length) {
                                row.addClass('ui-expanded-row');
                                this.displayExpandedRow(row, content);
                            }
                        }
                    });

                return true;
            },
            oncomplete: function() {
                $this.expansionProcess = $.grep($this.expansionProcess, function(r) {
                    return r !== rowIndex;
                });
            }
        };

        if(this.hasBehavior('rowToggle')) {
            var rowToggleBehavior = this.cfg.behaviors['rowToggle'];

            rowToggleBehavior.call(this, options);
        } 
        else {
            PrimeFaces.ajax.AjaxRequest(options); 
        }
    },
    
    displayExpandedRow: function(row, content) {
        row.after(content);
    },
    
    fireRowCollapseEvent: function(row) {
        var rowIndex = this.getRowMeta(row).index;
        
        if(this.hasBehavior('rowToggle')) {
            var ext = {
                params: [
                {
                    name: this.id + '_collapsedRowIndex', 
                    value: rowIndex
                }
                ]
            };
        
            var rowToggleBehavior = this.cfg.behaviors['rowToggle'];

            rowToggleBehavior.call(this, ext);
        } 
    },
    
    collapseRow: function(row) {
        row.removeClass('ui-expanded-row').next('.ui-expanded-row-content').remove();
    },
    
    collapseAllRows: function() {
        var $this = this;
        
        this.getExpandedRows().each(function() {
            var expandedRow = $(this);
            $this.collapseRow(expandedRow);

            var columns = expandedRow.children('td');
            for(var i = 0; i < columns.length; i++) {
                var column = columns.eq(i),
                toggler = column.children('.ui-row-toggler');

                if(toggler.length > 0) {
                    toggler.addClass('ui-icon-circle-triangle-e').removeClass('ui-icon-circle-triangle-s');
                    break;
                }
            }
        });
    },
    
    getExpandedRows: function() {
        return this.tbody.children('.ui-expanded-row');
    },
        
    /**
     * Binds editor events non-obstrusively
     */
    bindEditEvents: function() {
        var $this = this;
        this.cfg.cellSeparator = this.cfg.cellSeparator||' ';
        
        if(this.cfg.editMode === 'row') {
            var rowEditorSelector = '> tr > td > div.ui-row-editor';
            
            this.tbody.off('click.datatable', rowEditorSelector)
                        .on('click.datatable', rowEditorSelector, null, function(e) {
                            var element = $(e.target),
                            row = element.closest('tr');
                            
                            if(element.hasClass('ui-icon-pencil')) {
                                $this.switchToRowEdit(row);
                                element.hide().siblings().show();
                            }
                            else if(element.hasClass('ui-icon-check')) {
                                $this.saveRowEdit(row);
                            }
                            else if(element.hasClass('ui-icon-close')) {
                                $this.cancelRowEdit(row);
                            }
                        });
        }
        else if(this.cfg.editMode === 'cell') {
            var cellSelector = '> tr > td.ui-editable-column';
            
            this.tbody.off('click.datatable-cell', cellSelector)
                        .on('click.datatable-cell', cellSelector, null, function(e) {
                            $this.incellClick = true;
                            
                            var cell = $(this);
                            if(!cell.hasClass('ui-cell-editing')) {
                                $this.showCellEditor($(this));
                            }
                        });
                        
            $(document).off('click.datatable-cell-blur' + this.id)
                        .on('click.datatable-cell-blur' + this.id, function(e) {                            
                            if(!$this.incellClick && $this.currentCell && !$this.contextMenuClick) {
                                $this.saveCell($this.currentCell);
                            }
                            
                            $this.incellClick = false;
                            $this.contextMenuClick = false;
                        });
        }
    },
    
    switchToRowEdit: function(row) {
        this.showRowEditors(row);
        
        if(this.hasBehavior('rowEditInit')) {
            var rowEditInitBehavior = this.cfg.behaviors['rowEditInit'],
            rowIndex = this.getRowMeta(row).index;
            
            var ext = {
                params: [{name: this.id + '_rowEditIndex', value: rowIndex}]
            };

            rowEditInitBehavior.call(this, ext);
        }
    },
    
    showRowEditors: function(row) {
        row.addClass('ui-state-highlight ui-row-editing').children('td.ui-editable-column').each(function() {
            var column = $(this);

            column.find('.ui-cell-editor-output').hide();
            column.find('.ui-cell-editor-input').show();
        });
    },
    
    showCellEditor: function(c) {
        this.incellClick = true;
        
        var cell = null,
        $this = this;
                    
        if(c) {
            cell = c;
                        
            //remove contextmenu selection highlight
            if(this.contextMenuCell) {
                this.contextMenuCell.parent().removeClass('ui-state-highlight');
            }
        }
        else {
            cell = this.contextMenuCell;
        }
        
        if(this.currentCell) {
            $this.saveCell(this.currentCell);
        }
        
        this.currentCell = cell;
                
        var cellEditor = cell.children('div.ui-cell-editor'),
        displayContainer = cellEditor.children('div.ui-cell-editor-output'),
        inputContainer = cellEditor.children('div.ui-cell-editor-input'),
        inputs = inputContainer.find(':input:enabled'),
        multi = inputs.length > 1;
                                        
        cell.addClass('ui-state-highlight ui-cell-editing');
        displayContainer.hide();
        inputContainer.show();
        inputs.eq(0).focus().select();
        
        //metadata
        if(multi) {
            var oldValues = [];
            for(var i = 0; i < inputs.length; i++) {
                oldValues.push(inputs.eq(i).val());
            }
            
            cell.data('multi-edit', true);
            cell.data('old-value', oldValues);
        } 
        else {
            cell.data('multi-edit', false);
            cell.data('old-value', inputs.eq(0).val());
        }
        
        //bind events on demand
        if(!cell.data('edit-events-bound')) {
            cell.data('edit-events-bound', true);
            
            inputs.on('keydown.datatable-cell', function(e) {
                    var keyCode = $.ui.keyCode,
                    shiftKey = e.shiftKey,
                    key = e.which,
                    input = $(this);

                    if(key === keyCode.ENTER || key == keyCode.NUMPAD_ENTER) {
                        $this.saveCell(cell);

                        e.preventDefault();
                    }
                    else if(key === keyCode.TAB) {
                        if(multi) {
                            var focusIndex = shiftKey ? input.index() - 1 : input.index() + 1;

                            if(focusIndex < 0 || (focusIndex === inputs.length)) {
                                $this.tabCell(cell, !shiftKey);                                
                            } else {
                                inputs.eq(focusIndex).focus();
                            }
                        }
                        else {
                            $this.tabCell(cell, !shiftKey);
                        }
                        
                        e.preventDefault();
                    }
                })
                .on('focus.datatable-cell click.datatable-cell', function(e) {
                    $this.currentCell = cell;
                });
        }        
    },
    
    tabCell: function(cell, forward) {
        var targetCell = forward ? cell.next() : cell.prev();
        if(targetCell.length == 0) {
            var tabRow = forward ? cell.parent().next() : cell.parent().prev();
            targetCell = forward ? tabRow.children('td.ui-editable-column:first') : tabRow.children('td.ui-editable-column:last');
        }

        this.showCellEditor(targetCell);
    },
    
    saveCell: function(cell) {
        var inputs = cell.find('div.ui-cell-editor-input :input:enabled'),
        changed = false,
        $this = this;
        
        if(cell.data('multi-edit')) {
            var oldValues = cell.data('old-value');
            for(var i = 0; i < inputs.length; i++) {
                if(inputs.eq(i).val() != oldValues[i]) {
                    changed = true;
                    break;
                }
            }
        } 
        else {
            changed = (inputs.eq(0).val() != cell.data('old-value'));
        }
        
        if(changed)
            $this.doCellEditRequest(cell);
        else
            $this.viewMode(cell);
        
        this.currentCell = null;
    },
        
    viewMode: function(cell) {
        var cellEditor = cell.children('div.ui-cell-editor'),
        editableContainer = cellEditor.children('div.ui-cell-editor-input'),
        displayContainer = cellEditor.children('div.ui-cell-editor-output');
        
        cell.removeClass('ui-cell-editing ui-state-error ui-state-highlight');
        displayContainer.show();
        editableContainer.hide();
        cell.removeData('old-value').removeData('multi-edit');
    },
    
    doCellEditRequest: function(cell) {
        var rowMeta = this.getRowMeta(cell.closest('tr')),
        cellEditor = cell.children('.ui-cell-editor'),
        cellEditorId = cellEditor.attr('id'),
        cellIndex = cell.index(),
        cellInfo = rowMeta.index + ',' + cellIndex,
        $this = this;

        var options = {
            source: this.id,
            process: this.id,
            update: this.id,
            params: [{name: this.id + '_encodeFeature', value: true},
                     {name: this.id + '_cellInfo', value: cellInfo},
                     {name: cellEditorId, value: cellEditorId}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                        widget: $this,
                        handle: function(content) {
                            cellEditor.children('.ui-cell-editor-output').html(content);
                        }
                    });

                return true;
            },
            oncomplete: function(xhr, status, args) {                            
                if(args.validationFailed)
                    cell.addClass('ui-state-error');
                else
                    $this.viewMode(cell);
            }
        };

        if(this.hasBehavior('cellEdit')) {
            this.cfg.behaviors['cellEdit'].call(this, options);
        } 
        else {
            PrimeFaces.ajax.Request.handle(options);
        }
    },
    
    /**
     * Saves the edited row
     */
    saveRowEdit: function(rowEditor) {
        this.doRowEditRequest(rowEditor, 'save');
    },
    
    /**
     * Cancels row editing
     */
    cancelRowEdit: function(rowEditor) {
        this.doRowEditRequest(rowEditor, 'cancel');
    },
    
    /**
     * Sends an ajax request to handle row save or cancel
     */
    doRowEditRequest: function(rowEditor, action) {
        var row = rowEditor.closest('tr'),
        rowIndex = this.getRowMeta(row).index,
        expanded = row.hasClass('ui-expanded-row'),
        $this = this,
        options = {
            source: this.id,
            process: this.id,
            update: this.id,
            formId: this.cfg.formId,
            params: [{name: this.id + '_rowEditIndex', value: this.getRowMeta(row).index},
                     {name: this.id + '_rowEditAction', value: action},
                     {name: this.id + '_encodeFeature', value: true}],
            onsuccess: function(responseXML, status, xhr) {
                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                        widget: $this,
                        handle: function(content) {
                            if(expanded) {
                                this.collapseRow(row);
                            }

                            this.updateRow(row, content);
                        }
                    });

                return true;
            },
            oncomplete: function(xhr, status, args) {
                if(args && args.validationFailed) {
                    $this.invalidateRow(rowIndex);
                }
            }
        };
        
        if(action === 'save') {
            this.getRowEditors(row).each(function() {
                options.params.push({name: this.id, value: this.id});
            });
        }

        if(action === 'save' && this.hasBehavior('rowEdit')) {
            this.cfg.behaviors['rowEdit'].call(this, options);
        }
        else if(action === 'cancel' && this.hasBehavior('rowEditCancel')) {
            this.cfg.behaviors['rowEditCancel'].call(this, options);
        }
        else {
            PrimeFaces.ajax.Request.handle(options); 
        }
    },
    
    /*
     * Updates row with given content
     */
    updateRow: function(row, content) {
        row.replaceWith(content);
    },
    
    /**
     * Displays row editors in invalid format
     */
    invalidateRow: function(index) {
        this.tbody.children('tr').eq(index).addClass('ui-widget-content ui-row-editing ui-state-error');
    },
    
    /**
     * Finds all editors of a row
     */
    getRowEditors: function(row) {
        return row.find('div.ui-cell-editor');
    },

    /**
     * Returns the paginator instance if any defined
     */
    getPaginator: function() {
        return this.paginator;
    },
    
    /**
     * Writes selected row ids to state holder
     */
    writeSelections: function() {
        $(this.selectionHolder).val(this.selection.join(','));
    },
    
    isSingleSelection: function() {
        return this.cfg.selectionMode == 'single';
    },
    
    isMultipleSelection: function() {
        return this.cfg.selectionMode == 'multiple' || this.isCheckboxSelectionEnabled();
    },
    
    /**
     * Clears the selection state
     */
    clearSelection: function() {
        this.selection = [];

        $(this.selectionHolder).val('');
    },
    
    /**
     * Returns true|false if selection is enabled|disabled
     */
    isSelectionEnabled: function() {
        return this.cfg.selectionMode != undefined || this.cfg.columnSelectionMode != undefined;
    },

    /**
     * Returns true|false if checkbox selection is enabled|disabled
     */
    isCheckboxSelectionEnabled: function() {
        return this.cfg.selectionMode === 'checkbox';
    },
    
    /**
     * Returns true|false if radio selection is enabled|disabled
     */
    isRadioSelectionEnabled: function() {
        return this.cfg.selectionMode === 'radio';
    },
            
    /**
     * Clears table filters
     */
    clearFilters: function() {
        this.thead.find('> tr > th.ui-filter-column > .ui-column-filter').val('');
        $(this.jqId + '\\:globalFilter').val('');

        this.filter();
    },
    
    setupResizableColumns: function() {
        this.cfg.resizeMode = this.cfg.resizeMode||'fit';
        this.hasColumnGroup = this.hasColGroup();
        if(this.hasColumnGroup) {
            this.addGhostRow();
        }
        
        this.fixColumnWidths();
        
        if(!this.cfg.liveResize) {
            this.resizerHelper = $('<div class="ui-column-resizer-helper ui-state-highlight"></div>').appendTo(this.jq);
        }
        
        this.addResizers();
        
        var resizers = this.thead.find('> tr > th > span.ui-column-resizer'),
        $this = this;
            
        resizers.draggable({
            axis: 'x',
            start: function(event, ui) {
                ui.helper.data('originalposition', ui.helper.offset());
                
                if($this.cfg.liveResize) {
                    $this.jq.css('cursor', 'col-resize');
                }
                else {
                    var height = $this.cfg.scrollable ? $this.scrollBody.height() : $this.thead.parent().height() - $this.thead.height() - 1;
                    $this.resizerHelper.height(height);
                    $this.resizerHelper.show();
                }
            },
            drag: function(event, ui) {
                if($this.cfg.liveResize) {
                    $this.resize(event, ui);
                }
                else {
                    $this.resizerHelper.offset({
                        left: ui.helper.offset().left + ui.helper.width() / 2, 
                        top: $this.thead.offset().top + $this.thead.height()
                    });  
                }                
            },
            stop: function(event, ui) {
                var columnHeader = ui.helper.parent();
                ui.helper.css({
                    'left': '',
                    'top': '0px'
                });
                
                if($this.cfg.liveResize) {
                    $this.jq.css('cursor', 'default');
                } else {
                    $this.resize(event, ui);
                    $this.resizerHelper.hide();
                }
                
                var options = {
                    source: $this.id,
                    process: $this.id,
                    params: [
                        {name: $this.id + '_colResize', value: true},
                        {name: $this.id + '_columnId', value: columnHeader.attr('id')},
                        {name: $this.id + '_width', value: columnHeader.width()},
                        {name: $this.id + '_height', value: columnHeader.height()}
                    ]
                }
                
                if($this.hasBehavior('colResize')) {
                    $this.cfg.behaviors['colResize'].call($this, options);
                }
                
                if($this.cfg.stickyHeader) {
                    $this.thead.find('.ui-column-filter').prop('disabled', false);
                    $this.clone = $this.thead.clone(true);
                    $this.cloneContainer.find('thead').remove();
                    $this.cloneContainer.children('table').append($this.clone);
                    $this.thead.find('.ui-column-filter').prop('disabled', true);
                }
            },
            containment: this.jq
        });
    },
    
    hasColGroup: function() {
        return this.thead.children('tr').length > 1;
    },
    
    addGhostRow: function() {
        var dataColumnsCount = this.tbody.find('tr:first').children('td').length,
        columnMarkup = '';

        for(var i = 0; i < dataColumnsCount; i++) {
            columnMarkup += '<th style="height:0px;border-bottom-width: 0px;border-top-width: 0px;padding-top: 0px;padding-bottom: 0px;outline: 0 none;" class="ui-resizable-column"></th>';
        }
        
        this.thead.prepend('<tr>' + columnMarkup + '</tr>');

        if(this.cfg.scrollable) {         
            this.theadClone.prepend('<tr>' + columnMarkup + '</tr>');
            this.footerTable.children('tfoot').prepend('<tr>' + columnMarkup + '</tr>');
        }
    },
    
    findGroupResizer: function(ui) {
        for(var i = 0; i < this.groupResizers.length; i++) {
            var groupResizer = this.groupResizers.eq(i);
            if(groupResizer.offset().left === ui.helper.data('originalposition').left) {
                return groupResizer;
            }
        }
        
        return null;
    },
    
    addResizers: function() {
        var resizableColumns = this.thead.find('> tr > th.ui-resizable-column');
        resizableColumns.prepend('<span class="ui-column-resizer">&nbsp;</span>');
        
        if(this.cfg.resizeMode === 'fit') {
            resizableColumns.filter(':last-child').children('span.ui-column-resizer').hide();
        }
        
        if(this.hasColumnGroup) {
            this.groupResizers = this.thead.find('> tr:first > th > .ui-column-resizer');
        }
    },
    
    resize: function(event, ui) {
        var columnHeader, nextColumnHeader, change = null, newWidth = null, nextColumnWidth = null, 
        expandMode = (this.cfg.resizeMode === 'expand'),
        table = this.thead.parent();
                
        if(this.hasColumnGroup) {
            var groupResizer = this.findGroupResizer(ui);
            if(!groupResizer) {
                return;
            }
            
            columnHeader = groupResizer.parent();
        }
        else {
            columnHeader = ui.helper.parent();
        }
        
        var nextColumnHeader = columnHeader.next();
        
        if(this.cfg.liveResize) {
            change = columnHeader.outerWidth() - (event.pageX - columnHeader.offset().left),
            newWidth = (columnHeader.width() - change),
            nextColumnWidth = (nextColumnHeader.width() + change);
        }
        else {
            change = (ui.position.left - ui.originalPosition.left),
            newWidth = (columnHeader.width() + change),
            nextColumnWidth = (nextColumnHeader.width() - change);
        }
                        
        if((newWidth > 15 && nextColumnWidth > 15) || (expandMode && newWidth > 15)) {          
            if(expandMode) {
                table.width(table.width() + change);
                setTimeout(function() {
                    columnHeader.width(newWidth);
                }, 1);
            }
            else {
                columnHeader.width(newWidth);
                nextColumnHeader.width(nextColumnWidth);
            }
            
            if(this.cfg.scrollable) {
                var cloneTable = this.theadClone.parent(),
                colIndex = columnHeader.index();
        
                if(expandMode) {
                    var $this = this;
                    
                    //body
                    cloneTable.width(cloneTable.width() + change);
                    
                    //footer
                    this.footerTable.width(this.footerTable.width() + change);
                    
                    setTimeout(function() {
                        if($this.hasColumnGroup) {
                            $this.theadClone.find('> tr:first').children('th').eq(colIndex).width(newWidth);            //body
                            $this.footerTable.find('> tfoot > tr:first').children('th').eq(colIndex).width(newWidth);   //footer
                        }
                        else {
                            $this.theadClone.find(PrimeFaces.escapeClientId(columnHeader.attr('id') + '_clone')).width(newWidth);   //body
                            $this.footerCols.eq(colIndex).width(newWidth);                                                          //footer
                        }
                    }, 1);
                }
                else {
                    if(this.hasColumnGroup) {
                        //body
                        this.theadClone.find('> tr:first').children('th').eq(colIndex).width(newWidth);
                        this.theadClone.find('> tr:first').children('th').eq(colIndex + 1).width(nextColumnWidth);
                        
                        //footer
                        this.footerTable.find('> tfoot > tr:first').children('th').eq(colIndex).width(newWidth);
                        this.footerTable.find('> tfoot > tr:first').children('th').eq(colIndex + 1).width(nextColumnWidth);
                    }
                    else {
                        //body
                        this.theadClone.find(PrimeFaces.escapeClientId(columnHeader.attr('id') + '_clone')).width(newWidth);
                        this.theadClone.find(PrimeFaces.escapeClientId(nextColumnHeader.attr('id') + '_clone')).width(nextColumnWidth);
                        
                        //footer
                        if(this.footerCols.length > 0) {
                            var footerCol = this.footerCols.eq(colIndex),
                            nextFooterCol = footerCol.next();

                            footerCol.width(newWidth);
                            nextFooterCol.width(nextColumnWidth);
                        }
                    }
                }
            }            
        }
    },
    
    hasBehavior: function(event) {
        if(this.cfg.behaviors) {
            return this.cfg.behaviors[event] != undefined;
        }
    
        return false;
    },
    
    /**
     * Remove given rowIndex from selection
     */
    removeSelection: function(rowIndex) {        
        this.selection = $.grep(this.selection, function(value) {
            return value != rowIndex;
        });
    },
    
    /**
     * Adds given rowIndex to selection if it doesn't exist already
     */
    addSelection: function(rowIndex) {
        if(!this.isSelected(rowIndex)) {
            this.selection.push(rowIndex);
        }
    },
    
    /**
     * Finds if given rowIndex is in selection
     */
    isSelected: function(rowIndex) {
        return PrimeFaces.inArray(this.selection, rowIndex);
    },
    
    getRowMeta: function(row) {
        var meta = {
            index: row.data('ri'),
            key:  row.attr('data-rk')
        };

        return meta;
    },
    
    setupDraggableColumns: function() {
        this.orderStateHolder = $(this.jqId + '_columnOrder');
        this.saveColumnOrder();
        
        this.dragIndicatorTop = $('<span class="ui-icon ui-icon-arrowthick-1-s" style="position:absolute"/></span>').hide().appendTo(this.jq);
        this.dragIndicatorBottom = $('<span class="ui-icon ui-icon-arrowthick-1-n" style="position:absolute"/></span>').hide().appendTo(this.jq);

        var $this = this;

        $(this.jqId + ' thead th').draggable({
            appendTo: 'body',
            opacity: 0.75,
            cursor: 'move',
            scope: this.id,
            cancel: ':input,.ui-column-resizer',
            drag: function(event, ui) {
                var droppable = ui.helper.data('droppable-column');

                if(droppable) {
                    var droppableOffset = droppable.offset(),
                    topArrowY = droppableOffset.top - 10,
                    bottomArrowY = droppableOffset.top + droppable.height() + 8,
                    arrowX = null;
                    
                    //calculate coordinates of arrow depending on mouse location
                    if(event.originalEvent.pageX >= droppableOffset.left + (droppable.width() / 2)) {
                        var nextDroppable = droppable.next();
                        if(nextDroppable.length == 1)
                            arrowX = nextDroppable.offset().left - 9;
                        else
                            arrowX = droppable.offset().left + droppable.innerWidth() - 9;
                            
                        ui.helper.data('drop-location', 1);     //right
                    }
                    else {
                        arrowX = droppableOffset.left  - 9;
                        ui.helper.data('drop-location', -1);    //left
                    }
                    
                    $this.dragIndicatorTop.offset({
                        'left': arrowX, 
                        'top': topArrowY - 3
                    }).show();
                    
                    $this.dragIndicatorBottom.offset({
                        'left': arrowX, 
                        'top': bottomArrowY - 3
                    }).show();
                }
            },
            stop: function(event, ui) {
                //hide dnd arrows
                $this.dragIndicatorTop.css({
                    'left':0, 
                    'top':0
                }).hide();
                
                $this.dragIndicatorBottom.css({
                    'left':0, 
                    'top':0
                }).hide();
            },
            helper: function() {
                var header = $(this),
                helper = $('<div class="ui-widget ui-state-default" style="padding:4px 10px;text-align:center;"></div>');

                helper.width(header.width());
                helper.height(header.height());

                helper.html(header.html());

                return helper.get(0);
            }
        }).droppable({
            hoverClass:'ui-state-highlight',
            tolerance:'pointer',
            scope: this.id,
            over: function(event, ui) {
                ui.helper.data('droppable-column', $(this));
            },
            drop: function(event, ui) {
                var draggedColumn = ui.draggable,
                dropLocation = ui.helper.data('drop-location'),
                droppedColumn =  $(this);
                                
                var draggedCells = $this.tbody.find('> tr:not(.ui-expanded-row-content) > td:nth-child(' + (draggedColumn.index() + 1) + ')'),
                droppedCells = $this.tbody.find('> tr:not(.ui-expanded-row-content) > td:nth-child(' + (droppedColumn.index() + 1) + ')');
                
                //drop right
                if(dropLocation > 0) {
                    if($this.cfg.resizableColumns) {
                        if(droppedColumn.next().length == 0) {
                            droppedColumn.children('span.ui-column-resizer').show();
                            draggedColumn.children('span.ui-column-resizer').hide();
                        }
                    }
                    
                    draggedColumn.insertAfter(droppedColumn);

                    draggedCells.each(function(i, item) {
                        $(this).insertAfter(droppedCells.eq(i));
                    });
                }
                //drop left
                else {
                    draggedColumn.insertBefore(droppedColumn);

                    draggedCells.each(function(i, item) {
                        $(this).insertBefore(droppedCells.eq(i));
                    });
                }
                
                //align widths
                if($this.cfg.scrollable) {
                    $this.columnWidthsFixed = false;
                    $this.fixColumnWidths();
                }
               
                //save order
                $this.saveColumnOrder();

                //fire colReorder event
                if($this.cfg.behaviors) {
                    var columnReorderBehavior = $this.cfg.behaviors['colReorder'];

                    if(columnReorderBehavior) {            
                        columnReorderBehavior.call($this);
                    }
                }
            }
        });
    },
    
    saveColumnOrder: function() {
        var columnIds = [],
        columns = $(this.jqId + ' thead:first th');

        columns.each(function(i, item) {
            columnIds.push($(item).attr('id'));
        });

        this.orderStateHolder.val(columnIds.join(','));
    },
    
    makeRowsDraggable: function() {
        var $this = this;
        
        this.tbody.sortable({
            placeholder: 'ui-datatable-rowordering ui-state-active',
            cursor: 'move',
            handle: 'td,span:not(.ui-c)',
            appendTo: document.body,
            helper: function(event, ui) {
                var cells = ui.children(),
                helper = $('<div class="ui-datatable ui-widget"><table><tbody></tbody></table></div>'),
                helperRow = ui.clone(),
                helperCells = helperRow.children();

                for(var i = 0; i < helperCells.length; i++) {
                    helperCells.eq(i).width(cells.eq(i).width());
                }
                
                helperRow.appendTo(helper.find('tbody'));

                return helper;
            },
            update: function(event, ui) {
                var fromIndex = ui.item.data('ri'),
                toIndex = $this.paginator ? $this.paginator.getFirst() + ui.item.index(): ui.item.index();

                $this.syncRowParity();
                
                var options = {
                    source: $this.id,
                    process: $this.id,
                    params: [
                        {name: $this.id + '_rowreorder', value: true},
                        {name: $this.id + '_fromIndex', value: fromIndex},
                        {name: $this.id + '_toIndex', value: toIndex},
                        {name: this.id + '_skipChildren', value: true}
                    ]
                }
                
                if($this.hasBehavior('rowReorder')) {
                    $this.cfg.behaviors['rowReorder'].call($this, options);
                } 
                else {
                    PrimeFaces.ajax.Request.handle(options);
                }
            }
        });
    },
    
    syncRowParity: function() {
        var rows = this.tbody.children('tr.ui-widget-content'),
        first = this.paginator ? this.paginator.getFirst(): 0;
        
        for(var i = first; i < rows.length; i++) {
            var row = rows.eq(i);
            
            row.data('ri', i).removeClass('ui-datatable-even ui-datatable-odd');
            
            if(i % 2 === 0)
                row.addClass('ui-datatable-even');
            else
                row.addClass('ui-datatable-odd');
                
        }
    },
    
    /**
     * Returns if there is any data displayed
     */
    isEmpty: function() {
        return this.tbody.children('tr.ui-datatable-empty-message').length === 1;
    },
    
    getSelectedRowsCount: function() {
        return this.isSelectionEnabled() ? this.selection.length : 0;
    },
    
    updateHeaderCheckbox: function() {
        if(this.isEmpty()) {
            this.uncheckHeaderCheckbox();
            this.disableHeaderCheckbox();
        }
        else {
            var checkboxes, selectedCheckboxes, enabledCheckboxes, disabledCheckboxes;
            
            if(this.cfg.nativeElements) {
                checkboxes = this.tbody.find('> tr > td.ui-selection-column > :checkbox');
                enabledCheckboxes = checkboxes.filter(':enabled');
                disabledCheckboxes = checkboxes.filter(':disabled');
                selectedCheckboxes = enabledCheckboxes.filter(':checked');
            }
            else {
                checkboxes = this.tbody.find('> tr > td.ui-selection-column .ui-chkbox-box');
                enabledCheckboxes = checkboxes.filter(':not(.ui-state-disabled)');
                disabledCheckboxes = checkboxes.filter('.ui-state-disabled');
                selectedCheckboxes = enabledCheckboxes.filter('.ui-state-active');
            }
                        
            if(enabledCheckboxes.length && enabledCheckboxes.length === selectedCheckboxes.length)
               this.checkHeaderCheckbox();
            else
               this.uncheckHeaderCheckbox();
               
            if(checkboxes.length === disabledCheckboxes.length)
               this.disableHeaderCheckbox();
            else
               this.enableHeaderCheckbox();
        }
    },
    
    checkHeaderCheckbox: function() {
        if(this.cfg.nativeElements)
            this.checkAllToggler.prop('checked', true);
        else
            this.checkAllToggler.addClass('ui-state-active').children('span.ui-chkbox-icon').removeClass('ui-icon-blank').addClass('ui-icon-check');
    },
    
    uncheckHeaderCheckbox: function() {
        if(this.cfg.nativeElements)
            this.checkAllToggler.prop('checked', false);
        else
            this.checkAllToggler.removeClass('ui-state-active').children('span.ui-chkbox-icon').addClass('ui-icon-blank').removeClass('ui-icon-check');
    },
    
    disableHeaderCheckbox: function() {
        if(this.cfg.nativeElements)
            this.checkAllToggler.prop('disabled', true);
        else
            this.checkAllToggler.addClass('ui-state-disabled');
    },
    
    enableHeaderCheckbox: function() {
        if(this.cfg.nativeElements)
            this.checkAllToggler.prop('disabled', false);
        else
            this.checkAllToggler.removeClass('ui-state-disabled');
    },
            
    setupStickyHeader: function() {
        var table = this.thead.parent(),
        offset = table.offset(),
        win = $(window),
        $this = this,
        stickyNS = 'scroll.' + this.id,
        resizeNS = 'resize.sticky-' + this.id; 

        this.cloneContainer = $('<div class="ui-datatable ui-datatable-sticky ui-widget"><table></table></div>');
        this.clone = this.thead.clone(true);
        this.cloneContainer.children('table').append(this.clone);
        
        this.cloneContainer.css({
            position: 'absolute',
            width: table.outerWidth(),
            top: offset.top,
            left: offset.left,
            'z-index': ++PrimeFaces.zindex
        })
        .appendTo(this.jq);

        win.off(stickyNS).on(stickyNS, function() {
            var scrollTop = win.scrollTop(),
            tableOffset = table.offset();
            
            if(scrollTop > tableOffset.top) {
                $this.cloneContainer.css({
                                        'position': 'fixed',
                                        'top': '0px'
                                    })
                                    .addClass('ui-shadow ui-sticky');
                
                if(scrollTop >= (tableOffset.top + $this.tbody.height()))
                    $this.cloneContainer.hide();
                else
                    $this.cloneContainer.show();
            }
            else {
                $this.cloneContainer.css({
                                        'position': 'absolute',
                                        'top': tableOffset.top
                                    })
                                    .removeClass('ui-shadow ui-sticky');
            }
        })
        .off(resizeNS).on(resizeNS, function() {
            $this.cloneContainer.width(table.outerWidth());
        });
        
        //filter support
        this.thead.find('.ui-column-filter').prop('disabled', true);
    }

});

/**
 * PrimeFaces DataTable with Frozen Columns Widget
 */
PrimeFaces.widget.FrozenDataTable = PrimeFaces.widget.DataTable.extend({
        
    setupScrolling: function() {
        this.scrollLayout = this.jq.find('> table > tbody > tr > td.ui-datatable-frozenlayout-right');
        this.frozenLayout = this.jq.find('> table > tbody > tr > td.ui-datatable-frozenlayout-left');
        this.scrollContainer = this.jq.find('> table > tbody > tr > td.ui-datatable-frozenlayout-right > .ui-datatable-scrollable-container');
        this.frozenContainer = this.jq.find('> table > tbody > tr > td.ui-datatable-frozenlayout-left > .ui-datatable-frozen-container');
        this.scrollHeader = this.scrollContainer.children('.ui-datatable-scrollable-header');
        this.scrollHeaderBox = this.scrollHeader.children('div.ui-datatable-scrollable-header-box');
        this.scrollBody = this.scrollContainer.children('.ui-datatable-scrollable-body');
        this.scrollFooter = this.scrollContainer.children('.ui-datatable-scrollable-footer');
        this.scrollFooterBox = this.scrollFooter.children('div.ui-datatable-scrollable-footer-box');
        this.scrollStateHolder = $(this.jqId + '_scrollState');
        this.scrollHeaderTable = this.scrollHeaderBox.children('table');
        this.scrollBodyTable = this.scrollBody.children('table');
        this.scrollThead = this.thead.eq(1);
        this.scrollTbody = this.tbody.eq(1);
        this.scrollFooterTable = this.scrollFooterBox.children('table');
        this.scrollFooterCols = this.scrollFooter.find('> .ui-datatable-scrollable-footer-box > table > tfoot > tr > td');  
        this.frozenHeader = this.frozenContainer.children('.ui-datatable-scrollable-header');
        this.frozenBody = this.frozenContainer.children('.ui-datatable-scrollable-body');
        this.frozenBodyTable = this.frozenBody.children('table');
        this.frozenThead = this.thead.eq(0);
        this.frozenTbody = this.tbody.eq(0);
        this.frozenFooter = this.frozenContainer.children('.ui-datatable-scrollable-footer');
        this.frozenFooterTable = this.frozenFooter.find('> .ui-datatable-scrollable-footer-box > table');
        this.frozenFooterCols = this.frozenFooter.find('> .ui-datatable-scrollable-footer-box > table > tfoot > tr > td');
        this.percentageScrollHeight = this.cfg.scrollHeight && (this.cfg.scrollHeight.indexOf('%') !== -1);
        this.percentageScrollWidth = this.cfg.scrollWidth && (this.cfg.scrollWidth.indexOf('%') !== -1);

        this.frozenThead.find('> tr > th').addClass('ui-frozen-column');
        
        var $this = this,
        scrollBarWidth = this.getScrollbarWidth() + 'px';

        if(this.cfg.scrollHeight) {
            if(this.percentageScrollHeight) {
                this.adjustScrollHeight();
            }
            
            if(this.hasVerticalOverflow()) {
                this.scrollHeaderBox.css('margin-right', scrollBarWidth);
                this.scrollFooterBox.css('margin-right', scrollBarWidth);
            }
        }

        this.fixColumnWidths();      

        if(this.cfg.scrollWidth) {
            if(this.percentageScrollWidth)
                this.adjustScrollWidth();
            else
                this.setScrollWidth(parseInt(this.cfg.scrollWidth));
            
            if(this.hasVerticalOverflow()) {
                if(PrimeFaces.env.browser.webkit === true)
                    this.frozenBody.append('<div style="height:' + scrollBarWidth + ';border:1px solid transparent"></div>');
                else if(PrimeFaces.isIE(8))
                    this.frozenBody.append('<div style="height:' + scrollBarWidth + '"></div>');
                else
                    this.frozenBodyTable.css('margin-bottom', scrollBarWidth);
            }
        }
        
        this.cloneHead();  
        
        this.restoreScrollState();

        if(this.cfg.liveScroll) {
            this.scrollOffset = 0;
            this.cfg.liveScrollBuffer = (100 - this.cfg.liveScrollBuffer) / 100;
            this.shouldLiveScroll = true;       
            this.loadingLiveScroll = false;
            this.allLoadedLiveScroll = $this.cfg.scrollStep >= $this.cfg.scrollLimit;            
        }

        this.scrollBody.scroll(function() {
            var scrollLeft = $this.scrollBody.scrollLeft(),
            scrollTop = $this.scrollBody.scrollTop();
            $this.scrollHeaderBox.css('margin-left', -scrollLeft);
            $this.scrollFooterBox.css('margin-left', -scrollLeft);
            $this.frozenBody.scrollTop(scrollTop);

            if($this.shouldLiveScroll) {
                var scrollTop = this.scrollTop,
                scrollHeight = this.scrollHeight,
                viewportHeight = this.clientHeight;

                if((scrollTop >= ((scrollHeight * $this.cfg.liveScrollBuffer) - (viewportHeight))) && $this.shouldLoadLiveScroll()) {
                    $this.loadLiveRows();
                }
            }

            $this.saveScrollState();
        });

        var resizeNS = 'resize.' + this.id;
        $(window).unbind(resizeNS).bind(resizeNS, function() {
            if($this.jq.is(':visible')) {
                if($this.percentageScrollHeight)
                    $this.adjustScrollHeight();

                if($this.percentageScrollWidth)
                    $this.adjustScrollWidth();
            }
        });
    },
    
    cloneHead: function() {
        this.frozenTheadClone = this.frozenThead.clone();
        this.frozenTheadClone.find('th').each(function() {
            var header = $(this);
            header.attr('id', header.attr('id') + '_clone');
            $(this).children().not('.ui-column-title').remove();
        });
        this.frozenTheadClone.removeAttr('id').addClass('ui-datatable-scrollable-theadclone').height(0).prependTo(this.frozenBodyTable);
        
        this.scrollTheadClone = this.scrollThead.clone();
        this.scrollTheadClone.find('th').each(function() {
            var header = $(this);
            header.attr('id', header.attr('id') + '_clone');
            $(this).children().not('.ui-column-title').remove();
        });
        this.scrollTheadClone.removeAttr('id').addClass('ui-datatable-scrollable-theadclone').height(0).prependTo(this.scrollBodyTable);
    },
    
    hasVerticalOverflow: function() {
        return this.scrollBodyTable.outerHeight() > this.scrollBody.outerHeight();
    },
            
    adjustScrollHeight: function() {
        var relativeHeight = this.jq.parent().innerHeight() * (parseInt(this.cfg.scrollHeight) / 100),
        tableHeaderHeight = this.jq.children('.ui-datatable-header').outerHeight(true),
        tableFooterHeight = this.jq.children('.ui-datatable-footer').outerHeight(true),
        scrollersHeight = (this.scrollHeader.innerHeight() + this.scrollFooter.innerHeight()),
        paginatorsHeight = this.paginator ? this.paginator.getContainerHeight(true) : 0,
        height = (relativeHeight - (scrollersHeight + paginatorsHeight + tableHeaderHeight + tableFooterHeight));
        
        this.scrollBody.height(height);
        this.frozenBody.height(height);
    },
    
    //@Override
    adjustScrollWidth: function() {
        var width = parseInt((this.scrollLayout.innerWidth() * (parseInt(this.cfg.scrollWidth) / 100)));
        this.setScrollWidth(width);
    },
    
    setScrollWidth: function(width) {
        var $this = this,
        headerWidth = width + this.frozenLayout.width();
        
        this.jq.children('.ui-widget-header').each(function() {
            $this.setOuterWidth($(this), headerWidth);
        });
        this.scrollHeader.width(width);
        this.scrollBody.css('margin-right', 0).width(width);
        this.scrollFooter.width(width);
    },
    
    fixColumnWidths: function() {        
        if(!this.columnWidthsFixed) {
            if(PrimeFaces.isIE(7)) {
                this.bodyTable.css('width', 'auto');
            }
            
            if(this.cfg.scrollable) {
                this._fixColumnWidths(this.scrollHeader, this.scrollFooterCols, this.scrollColgroup);
                this._fixColumnWidths(this.frozenHeader, this.frozenFooterCols, this.frozenColgroup);
            }
            else {
                this.jq.find('> .ui-datatable-tablewrapper > table > thead > tr > th').each(function() {
                    var col = $(this);
                    col.width(col.width());
                });
            }

            this.columnWidthsFixed = true;
        }
    },
    
    _fixColumnWidths: function(header, footerCols) {
        header.find('> .ui-datatable-scrollable-header-box > table > thead > tr > th').each(function() {
            var headerCol = $(this),
            colIndex = headerCol.index(),
            width = headerCol.width();

            headerCol.width(width);

            if(footerCols.length > 0) {
                var footerCol = footerCols.eq(colIndex);
                footerCol.width(width);
            }
        });
    },
    
    //@Override
    updateData: function(data, clear) {
        var table = $('<table><tbody>' + data + '</tbody></table>'),
        rows = table.find('> tbody > tr'),
        empty = (clear === undefined) ? true: clear; 

        if(empty) {
            this.frozenTbody.children().remove();
            this.scrollTbody.children().remove();
        }

        for(var i = 0; i < rows.length; i++) {
            var row = rows.eq(i),
            columns = row.children('td'),
            frozenRow = this.copyRow(row),
            scrollableRow = this.copyRow(row);
    
            frozenRow.append(columns.slice(0, this.cfg.frozenColumns));
            scrollableRow.append(columns.slice(this.cfg.frozenColumns));
            
            this.frozenTbody.append(frozenRow);
            this.scrollTbody.append(scrollableRow);
        }
        
        this.postUpdateData();
    },
    
    copyRow: function(original) {
        return $('<tr></tr>').data('ri', original.data('ri')).attr('data-rk', original.data('rk')).addClass(original.attr('class')).attr('role', 'row');
    },
    
    getThead: function() {
        return $(this.jqId + '_frozenThead,' + this.jqId + '_scrollableThead');
    },
    
    getTbody: function() {
        return $(this.jqId + '_frozenTbody,' + this.jqId + '_scrollableTbody');
    },
    
    bindRowHover: function(selector) {
        var $this = this;
        
        this.tbody.off('mouseover.datatable mouseout.datatable', selector)
                    .on('mouseover.datatable', selector, null, function() {
                        var row = $(this),
                        twinRow = $this.getTwinRow(row);
                
                        if(!row.hasClass('ui-state-highlight')) {
                            row.addClass('ui-state-hover');
                            twinRow.addClass('ui-state-hover');
                        }
                    })
                    .on('mouseout.datatable', selector, null, function() {
                        var row = $(this),
                        twinRow = $this.getTwinRow(row);

                        if(!row.hasClass('ui-state-highlight')) {
                            row.removeClass('ui-state-hover');
                            twinRow.removeClass('ui-state-hover');
                        }
                    });
    },
    
    getTwinRow: function(row) {
        var twinTbody = (this.tbody.index(row.parent()) === 0) ? this.tbody.eq(1) : this.tbody.eq(0);
        
        return twinTbody.children().eq(row.index());
    },
    
    //@Override
    highlightRow: function(row) {
        this._super(row);
        this._super(this.getTwinRow(row));
    },
    
    //@Override
    unhighlightRow: function(row) {
        this._super(row);
        this._super(this.getTwinRow(row));
    },
    
    //@Override
    displayExpandedRow: function(row, content) {
        var twinRow = this.getTwinRow(row);
        row.after(content);
        var expansionRow = row.next();
        expansionRow.show();
        
        twinRow.after('<tr class="ui-expanded-row-content ui-widget-content"><td></td></tr>');       
        twinRow.next().children('td').attr('colspan', twinRow.children('td').length).height(expansionRow.children('td').height());
    },
    
    //@Override
    collapseRow: function(row) {
        this._super(row);
        this._super(this.getTwinRow(row));
    },
    
    //@Override
    getExpandedRows: function() {
        return this.frozenTbody.children('.ui-expanded-row');
    },
    
    //@Override
    showRowEditors: function(row) {
        this._super(row);
        this._super(this.getTwinRow(row));
    },
    
    //@Override
    updateRow: function(row, content) {
        var table = $('<table><tbody>' + content + '</tbody></table>'),
        newRow = table.find('> tbody > tr'),
        columns = newRow.children('td'),
        frozenRow = this.copyRow(newRow),
        scrollableRow = this.copyRow(newRow),
        twinRow = this.getTwinRow(row);
    
        frozenRow.append(columns.slice(0, this.cfg.frozenColumns));
        scrollableRow.append(columns.slice(this.cfg.frozenColumns));

        row.replaceWith(frozenRow);
        twinRow.replaceWith(scrollableRow);
    },
    
    //@Override
    invalidateRow: function(index) {
        this.frozenTbody.children('tr').eq(index).addClass('ui-widget-content ui-row-editing ui-state-error');
        this.scrollTbody.children('tr').eq(index).addClass('ui-widget-content ui-row-editing ui-state-error');
    },
    
    //@Override
    getRowEditors: function(row) {
        return row.find('div.ui-cell-editor').add(this.getTwinRow(row).find('div.ui-cell-editor'));
    },
    
    //@Override
    findGroupResizer: function(ui) {
        var resizer = this._findGroupResizer(ui, this.frozenGroupResizers);
        if(resizer) {
            return resizer;
        }
        else {
            return this._findGroupResizer(ui, this.scrollGroupResizers);
        }
    },
    
    _findGroupResizer: function(ui, resizers) {
        for(var i = 0; i < resizers.length; i++) {
            var groupResizer = resizers.eq(i);
            if(groupResizer.offset().left === ui.helper.data('originalposition').left) {
                return groupResizer;
            }
        }
        
        return null;
    },
    
    //@Override
    addResizers: function() {
        var frozenColumns = this.frozenThead.find('> tr > th.ui-resizable-column'),
        scrollableColumns = this.scrollThead.find('> tr > th.ui-resizable-column');

        frozenColumns.prepend('<span class="ui-column-resizer">&nbsp;</span>');
        scrollableColumns.prepend('<span class="ui-column-resizer">&nbsp;</span>')
        
        if(this.cfg.resizeMode === 'fit') {
            frozenColumns.filter(':last-child').addClass('ui-frozen-column-last');
            scrollableColumns.filter(':last-child').children('span.ui-column-resizer').hide();
        }
        
        if(this.hasColumnGroup) {
            this.frozenGroupResizers = this.frozenThead.find('> tr:first > th > .ui-column-resizer');
            this.scrollGroupResizers = this.scrollThead.find('> tr:first > th > .ui-column-resizer');
        }
    },
       
    //@Override
    resize: function(event, ui) {
        var columnHeader = null,
        change = null, 
        newWidth = null, 
        nextColumnWidth = null, 
        expandMode = (this.cfg.resizeMode === 'expand');
        
        if(this.hasColumnGroup) {
            var groupResizer = this.findGroupResizer(ui);
            if(!groupResizer) {
                return;
            }
            
            columnHeader = groupResizer.parent();
        }
        else {
            columnHeader = ui.helper.parent();
        }
        
        var nextColumnHeader = columnHeader.next();
        
        var colIndex = columnHeader.index(),
        lastFrozen = columnHeader.hasClass('ui-frozen-column-last');

        if(this.cfg.liveResize) {
            change = columnHeader.outerWidth() - (event.pageX - columnHeader.offset().left),
            newWidth = (columnHeader.width() - change),
            nextColumnWidth = (nextColumnHeader.width() + change);
        } 
        else {
            change = (ui.position.left - ui.originalPosition.left),
            newWidth = (columnHeader.width() + change),
            nextColumnWidth = (nextColumnHeader.width() - change);
        }
        
        var shouldChange = (expandMode && newWidth > 15) || (lastFrozen ? (newWidth > 15) : (newWidth > 15 && nextColumnWidth > 15)); 
        if(shouldChange) {
            var frozenColumn = columnHeader.hasClass('ui-frozen-column'),
            theadClone = frozenColumn ? this.frozenTheadClone : this.scrollTheadClone,
            originalTable = frozenColumn ? this.frozenThead.parent() : this.scrollThead.parent(),
            cloneTable = theadClone.parent(),
            footerCols = frozenColumn ? this.frozenFooterCols : this.scrollFooterCols,
            footerTable = frozenColumn ? this.frozenFooterTable:  this.scrollFooterTable,
            $this = this;

            if(expandMode) {
                if(lastFrozen) {
                    this.frozenLayout.width(this.frozenLayout.width() + change);
                }
                
                //header
                originalTable.width(originalTable.width() + change);
                
                //body
                cloneTable.width(cloneTable.width() + change);
                
                //footer
                footerTable.width(footerTable.width() + change);
                
                setTimeout(function() {
                    columnHeader.width(newWidth);
                    
                    if($this.hasColumnGroup) {
                        theadClone.find('> tr:first').children('th').eq(colIndex).width(newWidth);                          //body
                        footerTable.find('> tfoot > tr:first').children('th').eq(colIndex).width(newWidth);                 //footer
                    }
                    else {
                        theadClone.find(PrimeFaces.escapeClientId(columnHeader.attr('id') + '_clone')).width(newWidth);     //body
                        footerCols.eq(colIndex).width(newWidth);                                                            //footer
                    }
                }, 1);
                
                
            }
            else {
                if(lastFrozen) {
                    this.frozenLayout.width(this.frozenLayout.width() + change);
                }
                
                columnHeader.width(newWidth);
                nextColumnHeader.width(nextColumnWidth);
                
                if(this.hasColumnGroup) {
                    //body
                    theadClone.find('> tr:first').children('th').eq(colIndex).width(newWidth);
                    theadClone.find('> tr:first').children('th').eq(colIndex + 1).width(nextColumnWidth);

                    //footer
                    footerTable.find('> tfoot > tr:first').children('th').eq(colIndex).width(newWidth);
                    footerTable.find('> tfoot > tr:first').children('th').eq(colIndex + 1).width(nextColumnWidth);
                }
                else {
                    theadClone.find(PrimeFaces.escapeClientId(columnHeader.attr('id') + '_clone')).width(newWidth);
                    theadClone.find(PrimeFaces.escapeClientId(nextColumnHeader.attr('id') + '_clone')).width(nextColumnWidth);

                    if(footerCols.length > 0) {
                        var footerCol = footerCols.eq(colIndex),
                        nextFooterCol = footerCol.next();

                        footerCol.width(newWidth);
                        nextFooterCol.width(nextColumnWidth);
                    }
                }
            }
        }
    },
    
    //@Override
    hasColGroup: function() {
        return this.frozenThead.children('tr').length > 1 || this.scrollThead.children('tr').length > 1;
    },
    
    //@Override
    addGhostRow: function() {
        this._addGhostRow(this.frozenTbody, this.frozenThead, this.frozenTheadClone, this.frozenFooter.find('table'), 'ui-frozen-column');
        this._addGhostRow(this.scrollTbody, this.scrollThead, this.scrollTheadClone, this.scrollFooterTable);
    },
    
    _addGhostRow: function(body, header, headerClone, footerTable, columnClass) {
        var dataColumns = body.find('tr:first').children('td'),
        dataColumnsCount = dataColumns.length,
        columnMarkup = '',
        columnStyleClass = columnClass ? 'ui-resizable-column ' + columnClass : 'ui-resizable-column';

        for(var i = 0; i < dataColumnsCount; i++) {
            columnMarkup += '<th style="height:0px;border-bottom-width: 0px;border-top-width: 0px;padding-top: 0px;padding-bottom: 0px;outline: 0 none;width:' + dataColumns.eq(i).width() + 'px" class="' + columnStyleClass + '"></th>';
        }
        
        header.prepend('<tr>' + columnMarkup + '</tr>');

        if(this.cfg.scrollable) {         
            headerClone.prepend('<tr>' + columnMarkup + '</tr>');
            footerTable.children('tfoot').prepend('<tr>' + columnMarkup + '</tr>');
        }
    }
    
});