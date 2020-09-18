/* ========================================================================
 * Bootstrap: modal.js v3.3.7
 * http://getbootstrap.com/javascript/#modals
 * ========================================================================
 * Copyright 2011-2016 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // MODAL CLASS DEFINITION
    // ======================

    var Modal = function (element, options) {
        this.options = options
        this.$body = $(document.body)
        this.$element = $(element)
        this.$dialog = this.$element.find('.modal-dialog')
        this.$backdrop = null
        this.isShown = null
        this.originalBodyPad = null
        this.scrollbarWidth = 0
        this.ignoreBackdropClick = false

        if (this.options.remote) {
            this.$element
                .find('.modal-content')
                .load(this.options.remote, $.proxy(function () {
                    this.$element.trigger('loaded.bs.modal')
                }, this))
        }
    }

    Modal.VERSION = '3.3.7'

    Modal.TRANSITION_DURATION = 300
    Modal.BACKDROP_TRANSITION_DURATION = 150

    Modal.DEFAULTS = {
        backdrop: true,
        keyboard: true,
        show: true
    }

    Modal.prototype.toggle = function (_relatedTarget) {
        return this.isShown ? this.hide() : this.show(_relatedTarget)
    }

    Modal.prototype.show = function (_relatedTarget) {
        var that = this
        var e = $.Event('show.bs.modal', {relatedTarget: _relatedTarget})

        this.$element.trigger(e)

        if (this.isShown || e.isDefaultPrevented()) return

        this.isShown = true

        this.checkScrollbar()
        this.setScrollbar()
        this.$body.addClass('modal-open')

        this.escape()
        this.resize()

        this.$element.on('click.dismiss.bs.modal', '[data-dismiss="modal"]', $.proxy(this.hide, this))

        this.$dialog.on('mousedown.dismiss.bs.modal', function () {
            that.$element.one('mouseup.dismiss.bs.modal', function (e) {
                if ($(e.target).is(that.$element)) that.ignoreBackdropClick = true
            })
        })

        this.backdrop(function () {
            var transition = $.support.transition && that.$element.hasClass('fade')

            if (!that.$element.parent().length) {
                that.$element.appendTo(that.$body) // don't move modals dom position
            }

            that.$element
                .show()
                .scrollTop(0)

            that.adjustDialog()

            if (transition) {
                that.$element[0].offsetWidth // force reflow
            }

            that.$element.addClass('in')

            that.enforceFocus()

            var e = $.Event('shown.bs.modal', {relatedTarget: _relatedTarget})

            transition ?
                that.$dialog // wait for modal to slide in
                    .one('bsTransitionEnd', function () {
                        that.$element.trigger('focus').trigger(e)
                    })
                    .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
                that.$element.trigger('focus').trigger(e)
        })
    }

    Modal.prototype.hide = function (e) {
        if (e) e.preventDefault()

        e = $.Event('hide.bs.modal')

        this.$element.trigger(e)

        if (!this.isShown || e.isDefaultPrevented()) return

        this.isShown = false

        this.escape()
        this.resize()

        $(document).off('focusin.bs.modal')

        this.$element
            .removeClass('in')
            .off('click.dismiss.bs.modal')
            .off('mouseup.dismiss.bs.modal')

        this.$dialog.off('mousedown.dismiss.bs.modal')

        $.support.transition && this.$element.hasClass('fade') ?
            this.$element
                .one('bsTransitionEnd', $.proxy(this.hideModal, this))
                .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
            this.hideModal()
    }

    Modal.prototype.enforceFocus = function () {
        $(document)
            .off('focusin.bs.modal') // guard against infinite focus loop
            .on('focusin.bs.modal', $.proxy(function (e) {
                if (document !== e.target &&
                    this.$element[0] !== e.target &&
                    !this.$element.has(e.target).length) {
                    this.$element.trigger('focus')
                }
            }, this))
    }

    Modal.prototype.escape = function () {
        if (this.isShown && this.options.keyboard) {
            this.$element.on('keydown.dismiss.bs.modal', $.proxy(function (e) {
                e.which == 27 && this.hide()
            }, this))
        } else if (!this.isShown) {
            this.$element.off('keydown.dismiss.bs.modal')
        }
    }

    Modal.prototype.resize = function () {
        if (this.isShown) {
            $(window).on('resize.bs.modal', $.proxy(this.handleUpdate, this))
        } else {
            $(window).off('resize.bs.modal')
        }
    }

    Modal.prototype.hideModal = function () {
        var that = this
        this.$element.hide()
        this.backdrop(function () {
            that.$body.removeClass('modal-open')
            that.resetAdjustments()
            that.resetScrollbar()
            that.$element.trigger('hidden.bs.modal')
        })
    }

    Modal.prototype.removeBackdrop = function () {
        this.$backdrop && this.$backdrop.remove()
        this.$backdrop = null
    }

    Modal.prototype.backdrop = function (callback) {
        var that = this
        var animate = this.$element.hasClass('fade') ? 'fade' : ''

        if (this.isShown && this.options.backdrop) {
            var doAnimate = $.support.transition && animate

            this.$backdrop = $(document.createElement('div'))
                .addClass('modal-backdrop ' + animate)
                .appendTo(this.$body)

            this.$element.on('click.dismiss.bs.modal', $.proxy(function (e) {
                if (this.ignoreBackdropClick) {
                    this.ignoreBackdropClick = false
                    return
                }
                if (e.target !== e.currentTarget) return
                this.options.backdrop == 'static'
                    ? this.$element[0].focus()
                    : this.hide()
            }, this))

            if (doAnimate) this.$backdrop[0].offsetWidth // force reflow

            this.$backdrop.addClass('in')

            if (!callback) return

            doAnimate ?
                this.$backdrop
                    .one('bsTransitionEnd', callback)
                    .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
                callback()

        } else if (!this.isShown && this.$backdrop) {
            this.$backdrop.removeClass('in')

            var callbackRemove = function () {
                that.removeBackdrop()
                callback && callback()
            }
            $.support.transition && this.$element.hasClass('fade') ?
                this.$backdrop
                    .one('bsTransitionEnd', callbackRemove)
                    .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
                callbackRemove()

        } else if (callback) {
            callback()
        }
    }

    // these following methods are used to handle overflowing modals

    Modal.prototype.handleUpdate = function () {
        this.adjustDialog()
    }

    Modal.prototype.adjustDialog = function () {
        var modalIsOverflowing = this.$element[0].scrollHeight > document.documentElement.clientHeight

        this.$element.css({
            paddingLeft: !this.bodyIsOverflowing && modalIsOverflowing ? this.scrollbarWidth : '',
            paddingRight: this.bodyIsOverflowing && !modalIsOverflowing ? this.scrollbarWidth : ''
        })
    }

    Modal.prototype.resetAdjustments = function () {
        this.$element.css({
            paddingLeft: '',
            paddingRight: ''
        })
    }

    Modal.prototype.checkScrollbar = function () {
        var fullWindowWidth = window.innerWidth
        if (!fullWindowWidth) { // workaround for missing window.innerWidth in IE8
            var documentElementRect = document.documentElement.getBoundingClientRect()
            fullWindowWidth = documentElementRect.right - Math.abs(documentElementRect.left)
        }
        this.bodyIsOverflowing = document.body.clientWidth < fullWindowWidth
        this.scrollbarWidth = this.measureScrollbar()
    }

    Modal.prototype.setScrollbar = function () {
        var bodyPad = parseInt((this.$body.css('padding-right') || 0), 10)
        this.originalBodyPad = document.body.style.paddingRight || ''
        if (this.bodyIsOverflowing) this.$body.css('padding-right', bodyPad + this.scrollbarWidth)
    }

    Modal.prototype.resetScrollbar = function () {
        this.$body.css('padding-right', this.originalBodyPad)
    }

    Modal.prototype.measureScrollbar = function () { // thx walsh
        var scrollDiv = document.createElement('div')
        scrollDiv.className = 'modal-scrollbar-measure'
        this.$body.append(scrollDiv)
        var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth
        this.$body[0].removeChild(scrollDiv)
        return scrollbarWidth
    }


    // MODAL PLUGIN DEFINITION
    // =======================

    function Plugin(option, _relatedTarget) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.modal')
            var options = $.extend({}, Modal.DEFAULTS, $this.data(), typeof option == 'object' && option)

            if (!data) $this.data('bs.modal', (data = new Modal(this, options)))
            if (typeof option == 'string') data[option](_relatedTarget)
            else if (options.show) data.show(_relatedTarget)
        })
    }

    var old = $.fn.modal

    $.fn.modal = Plugin
    $.fn.modal.Constructor = Modal


    // MODAL NO CONFLICT
    // =================

    $.fn.modal.noConflict = function () {
        $.fn.modal = old
        return this
    }


    // MODAL DATA-API
    // ==============

    $(document).on('click.bs.modal.data-api', '[data-toggle="modal"]', function (e) {
        var $this = $(this)
        var href = $this.attr('href')
        var $target = $($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) // strip for ie7
        var option = $target.data('bs.modal') ? 'toggle' : $.extend({remote: !/#/.test(href) && href}, $target.data(), $this.data())

        if ($this.is('a')) e.preventDefault()

        $target.one('show.bs.modal', function (showEvent) {
            if (showEvent.isDefaultPrevented()) return // only register focus restorer if modal will actually get shown
            $target.one('hidden.bs.modal', function () {
                $this.is(':visible') && $this.trigger('focus')
            })
        })
        Plugin.call($target, option, this)
    })

}(jQuery);


/*!
 * fullPage 3.0.3
 * https://github.com/alvarotrigo/fullPage.js
 *
 * @license GPLv3 for open source use only
 * or Fullpage Commercial License for commercial use
 * http://alvarotrigo.com/fullPage/pricing/
 *
 * Copyright (C) 2018 http://alvarotrigo.com/fullPage - A project by Alvaro Trigo
 */
(function (root, window, document, factory, undefined) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(function () {
            root.fullpage = factory(window, document);
            return root.fullpage;
        });
    } else if (typeof exports === 'object') {
        // Node. Does not work with strict CommonJS.
        module.exports = factory(window, document);
    } else {
        // Browser globals.
        window.fullpage = factory(window, document);
    }
}(this, window, document, function (window, document) {
    'use strict';

    // keeping central set of classnames and selectors
    var WRAPPER = 'fullpage-wrapper';
    var WRAPPER_SEL = '.' + WRAPPER;

    // slimscroll
    var SCROLLABLE = 'fp-scrollable';
    var SCROLLABLE_SEL = '.' + SCROLLABLE;

    // util
    var RESPONSIVE = 'fp-responsive';
    var NO_TRANSITION = 'fp-notransition';
    var DESTROYED = 'fp-destroyed';
    var ENABLED = 'fp-enabled';
    var VIEWING_PREFIX = 'fp-viewing';
    var ACTIVE = 'active';
    var ACTIVE_SEL = '.' + ACTIVE;
    var COMPLETELY = 'fp-completely';
    var COMPLETELY_SEL = '.' + COMPLETELY;

    // section
    var SECTION_DEFAULT_SEL = '.section';
    var SECTION = 'fp-section';
    var SECTION_SEL = '.' + SECTION;
    var SECTION_ACTIVE_SEL = SECTION_SEL + ACTIVE_SEL;
    var TABLE_CELL = 'fp-tableCell';
    var TABLE_CELL_SEL = '.' + TABLE_CELL;
    var AUTO_HEIGHT = 'fp-auto-height';
    var AUTO_HEIGHT_SEL = '.' + AUTO_HEIGHT;
    var NORMAL_SCROLL = 'fp-normal-scroll';
    var NORMAL_SCROLL_SEL = '.' + NORMAL_SCROLL;

    // section nav
    var SECTION_NAV = 'fp-nav';
    var SECTION_NAV_SEL = '#' + SECTION_NAV;
    var SECTION_NAV_TOOLTIP = 'fp-tooltip';
    var SECTION_NAV_TOOLTIP_SEL = '.' + SECTION_NAV_TOOLTIP;
    var SHOW_ACTIVE_TOOLTIP = 'fp-show-active';

    // slide
    var SLIDE_DEFAULT_SEL = '.slide';
    var SLIDE = 'fp-slide';
    var SLIDE_SEL = '.' + SLIDE;
    var SLIDE_ACTIVE_SEL = SLIDE_SEL + ACTIVE_SEL;
    var SLIDES_WRAPPER = 'fp-slides';
    var SLIDES_WRAPPER_SEL = '.' + SLIDES_WRAPPER;
    var SLIDES_CONTAINER = 'fp-slidesContainer';
    var SLIDES_CONTAINER_SEL = '.' + SLIDES_CONTAINER;
    var TABLE = 'fp-table';

    // slide nav
    var SLIDES_NAV = 'fp-slidesNav';
    var SLIDES_NAV_SEL = '.' + SLIDES_NAV;
    var SLIDES_NAV_LINK_SEL = SLIDES_NAV_SEL + ' a';
    var SLIDES_ARROW = 'fp-controlArrow';
    var SLIDES_ARROW_SEL = '.' + SLIDES_ARROW;
    var SLIDES_PREV = 'fp-prev';
    var SLIDES_PREV_SEL = '.' + SLIDES_PREV;
    var SLIDES_ARROW_PREV = SLIDES_ARROW + ' ' + SLIDES_PREV;
    var SLIDES_ARROW_PREV_SEL = SLIDES_ARROW_SEL + SLIDES_PREV_SEL;
    var SLIDES_NEXT = 'fp-next';
    var SLIDES_NEXT_SEL = '.' + SLIDES_NEXT;
    var SLIDES_ARROW_NEXT = SLIDES_ARROW + ' ' + SLIDES_NEXT;
    var SLIDES_ARROW_NEXT_SEL = SLIDES_ARROW_SEL + SLIDES_NEXT_SEL;

    function initialise(containerSelector, options) {
        var isLicenseValid = options && new RegExp('([\\d\\w]{8}-){3}[\\d\\w]{8}|OPEN-SOURCE-GPLV3-LICENSE').test(options.licenseKey) || document.domain.indexOf('alvarotrigo.com') > -1;

        //only once my friend!
        if (hasClass($('html'), ENABLED)) {
            displayWarnings();
            return;
        }

        // common jQuery objects
        var $htmlBody = $('html, body');
        var $body = $('body')[0];

        var FP = {};

        // Creating some defaults, extending them with any options that were provided
        options = deepExtend({
            //navigation
            menu: false,
            anchors: [],
            lockAnchors: false,
            navigation: false,
            navigationPosition: 'right',
            navigationTooltips: [],
            showActiveTooltip: false,
            slidesNavigation: false,
            slidesNavPosition: 'bottom',
            scrollBar: false,
            hybrid: false,

            //scrolling
            css3: true,
            scrollingSpeed: 700,
            autoScrolling: true,
            fitToSection: true,
            fitToSectionDelay: 1000,
            easing: 'easeInOutCubic',
            easingcss3: 'ease',
            loopBottom: false,
            loopTop: false,
            loopHorizontal: true,
            continuousVertical: false,
            continuousHorizontal: false,
            scrollHorizontally: false,
            interlockedSlides: false,
            dragAndMove: false,
            offsetSections: false,
            resetSliders: false,
            fadingEffect: false,
            normalScrollElements: null,
            scrollOverflow: false,
            scrollOverflowReset: false,
            scrollOverflowHandler: window.fp_scrolloverflow ? window.fp_scrolloverflow.iscrollHandler : null,
            scrollOverflowOptions: null,
            touchSensitivity: 5,
            normalScrollElementTouchThreshold: 5,
            bigSectionsDestination: null,

            //Accessibility
            keyboardScrolling: true,
            animateAnchor: true,
            recordHistory: true,

            //design
            controlArrows: true,
            controlArrowColor: '#fff',
            verticalCentered: true,
            sectionsColor: [],
            paddingTop: 0,
            paddingBottom: 0,
            fixedElements: null,
            responsive: 0, //backwards compabitility with responsiveWiddth
            responsiveWidth: 0,
            responsiveHeight: 0,
            responsiveSlides: false,
            parallax: false,
            parallaxOptions: {
                type: 'reveal',
                percentage: 62,
                property: 'translate'
            },

            //Custom selectors
            sectionSelector: SECTION_DEFAULT_SEL,
            slideSelector: SLIDE_DEFAULT_SEL,

            //events
            v2compatible: false,
            afterLoad: null,
            onLeave: null,
            afterRender: null,
            afterResize: null,
            afterReBuild: null,
            afterSlideLoad: null,
            onSlideLeave: null,
            afterResponsive: null,

            lazyLoading: true
        }, options);

        //flag to avoid very fast sliding for landscape sliders
        var slideMoving = false;

        var isTouchDevice = navigator.userAgent.match(/(iPhone|iPod|iPad|Android|playbook|silk|BlackBerry|BB10|Windows Phone|Tizen|Bada|webOS|IEMobile|Opera Mini)/);
        var isTouch = (('ontouchstart' in window) || (navigator.msMaxTouchPoints > 0) || (navigator.maxTouchPoints));
        var container = typeof containerSelector === 'string' ? $(containerSelector)[0] : containerSelector;
        var windowsHeight = getWindowHeight();
        var isResizing = false;
        var isWindowFocused = true;
        var lastScrolledDestiny;
        var lastScrolledSlide;
        var canScroll = true;
        var scrollings = [];
        var controlPressed;
        var startingSection;
        var isScrollAllowed = {};
        isScrollAllowed.m = {'up': true, 'down': true, 'left': true, 'right': true};
        isScrollAllowed.k = deepExtend({}, isScrollAllowed.m);
        var MSPointer = getMSPointer();
        var events = {
            touchmove: 'ontouchmove' in window ? 'touchmove' : MSPointer.move,
            touchstart: 'ontouchstart' in window ? 'touchstart' : MSPointer.down
        };
        var scrollBarHandler;

        // taken from https://github.com/udacity/ud891/blob/gh-pages/lesson2-focus/07-modals-and-keyboard-traps/solution/modal.js
        var focusableElementsString = 'a[href], area[href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, [tabindex="0"], [contenteditable]';

        //timeouts
        var resizeId;
        var afterSectionLoadsId;
        var afterSlideLoadsId;
        var scrollId;
        var scrollId2;
        var keydownId;
        var originals = deepExtend({}, options); //deep copy
        var activeAnimation;

        displayWarnings();

        //easeInOutCubic animation included in the plugin
        window.fp_easings = deepExtend(window.fp_easings, {
            easeInOutCubic: function (t, b, c, d) {
                if ((t /= d / 2) < 1) return c / 2 * t * t * t + b;
                return c / 2 * ((t -= 2) * t * t + 2) + b;
            }
        });

        /**
         * Sets the autoScroll option.
         * It changes the scroll bar visibility and the history of the site as a result.
         */
        function setAutoScrolling(value, type) {
            //removing the transformation
            if (!value) {
                silentScroll(0);
            }

            setVariableState('autoScrolling', value, type);

            var element = $(SECTION_ACTIVE_SEL)[0];

            if (options.autoScrolling && !options.scrollBar) {
                css($htmlBody, {
                    'overflow': 'hidden',
                    'height': '100%'
                });

                setRecordHistory(originals.recordHistory, 'internal');

                //for IE touch devices
                css(container, {
                    '-ms-touch-action': 'none',
                    'touch-action': 'none'
                });

                if (element != null) {
                    //moving the container up
                    silentScroll(element.offsetTop);
                }

            } else {
                css($htmlBody, {
                    'overflow': 'visible',
                    'height': 'initial'
                });

                setRecordHistory(false, 'internal');

                //for IE touch devices
                css(container, {
                    '-ms-touch-action': '',
                    'touch-action': ''
                });

                //scrolling the page to the section with no animation
                if (element != null) {
                    var scrollSettings = getScrollSettings(element.offsetTop);
                    scrollSettings.element.scrollTo(0, scrollSettings.options);
                }
            }
        }

        /**
         * Defines wheter to record the history for each hash change in the URL.
         */
        function setRecordHistory(value, type) {
            setVariableState('recordHistory', value, type);
        }

        /**
         * Defines the scrolling speed
         */
        function setScrollingSpeed(value, type) {
            setVariableState('scrollingSpeed', value, type);
        }

        /**
         * Sets fitToSection
         */
        function setFitToSection(value, type) {
            setVariableState('fitToSection', value, type);
        }

        /**
         * Sets lockAnchors
         */
        function setLockAnchors(value) {
            options.lockAnchors = value;
        }

        /**
         * Adds or remove the possibility of scrolling through sections by using the mouse wheel or the trackpad.
         */
        function setMouseWheelScrolling(value) {
            if (value) {
                addMouseWheelHandler();
                addMiddleWheelHandler();
            } else {
                removeMouseWheelHandler();
                removeMiddleWheelHandler();
            }
        }

        /**
         * Adds or remove the possibility of scrolling through sections by using the mouse wheel/trackpad or touch gestures.
         * Optionally a second parameter can be used to specify the direction for which the action will be applied.
         *
         * @param directions string containing the direction or directions separated by comma.
         */
        function setAllowScrolling(value, directions) {
            if (typeof directions !== 'undefined') {
                directions = directions.replace(/ /g, '').split(',');

                directions.forEach(function (direction) {
                    setIsScrollAllowed(value, direction, 'm');
                });
            } else {
                setIsScrollAllowed(value, 'all', 'm');
            }
        }

        /**
         * Adds or remove the mouse wheel hijacking
         */
        function setMouseHijack(value) {
            if (value) {
                setMouseWheelScrolling(true);
                addTouchHandler();
            } else {
                setMouseWheelScrolling(false);
                removeTouchHandler();
            }
        }

        /**
         * Adds or remove the possibility of scrolling through sections by using the keyboard arrow keys
         */
        function setKeyboardScrolling(value, directions) {
            if (typeof directions !== 'undefined') {
                directions = directions.replace(/ /g, '').split(',');

                directions.forEach(function (direction) {
                    setIsScrollAllowed(value, direction, 'k');
                });
            } else {
                setIsScrollAllowed(value, 'all', 'k');
                options.keyboardScrolling = value;
            }
        }

        /**
         * Moves the page up one section.
         */
        function moveSectionUp() {
            var prev = prevUntil($(SECTION_ACTIVE_SEL)[0], SECTION_SEL);

            //looping to the bottom if there's no more sections above
            if (!prev && (options.loopTop || options.continuousVertical)) {
                prev = last($(SECTION_SEL));
            }

            if (prev != null) {
                scrollPage(prev, null, true);
            }
        }

        /**
         * Moves the page down one section.
         */
        function moveSectionDown() {
            var next = nextUntil($(SECTION_ACTIVE_SEL)[0], SECTION_SEL);

            //looping to the top if there's no more sections below
            if (!next &&
                (options.loopBottom || options.continuousVertical)) {
                next = $(SECTION_SEL)[0];
            }

            if (next != null) {
                scrollPage(next, null, false);
            }
        }

        /**
         * Moves the page to the given section and slide with no animation.
         * Anchors or index positions can be used as params.
         */
        function silentMoveTo(sectionAnchor, slideAnchor) {
            setScrollingSpeed(0, 'internal');
            moveTo(sectionAnchor, slideAnchor);
            setScrollingSpeed(originals.scrollingSpeed, 'internal');
        }

        /**
         * Moves the page to the given section and slide.
         * Anchors or index positions can be used as params.
         */
        function moveTo(sectionAnchor, slideAnchor) {
            var destiny = getSectionByAnchor(sectionAnchor);

            if (typeof slideAnchor !== 'undefined') {
                scrollPageAndSlide(sectionAnchor, slideAnchor);
            } else if (destiny != null) {
                scrollPage(destiny);
            }
        }

        /**
         * Slides right the slider of the active section.
         * Optional `section` param.
         */
        function moveSlideRight(section) {
            moveSlide('right', section);
        }

        /**
         * Slides left the slider of the active section.
         * Optional `section` param.
         */
        function moveSlideLeft(section) {
            moveSlide('left', section);
        }

        /**
         * When resizing is finished, we adjust the slides sizes and positions
         */
        function reBuild(resizing) {
            if (hasClass(container, DESTROYED)) {
                return;
            }  //nothing to do if the plugin was destroyed

            isResizing = true;

            windowsHeight = getWindowHeight();  //updating global var

            var sections = $(SECTION_SEL);
            for (var i = 0; i < sections.length; ++i) {
                var section = sections[i];
                var slidesWrap = $(SLIDES_WRAPPER_SEL, section)[0];
                var slides = $(SLIDE_SEL, section);

                //adjusting the height of the table-cell for IE and Firefox
                if (options.verticalCentered) {
                    css($(TABLE_CELL_SEL, section), {'height': getTableHeight(section) + 'px'});
                }

                css(section, {'height': windowsHeight + 'px'});

                //adjusting the position fo the FULL WIDTH slides...
                if (slides.length > 1) {
                    landscapeScroll(slidesWrap, $(SLIDE_ACTIVE_SEL, slidesWrap)[0]);
                }
            }

            if (options.scrollOverflow) {
                scrollBarHandler.createScrollBarForAll();
            }

            var activeSection = $(SECTION_ACTIVE_SEL)[0];
            var sectionIndex = index(activeSection, SECTION_SEL);

            //isn't it the first section?
            if (sectionIndex) {
                //adjusting the position for the current section
                silentMoveTo(sectionIndex + 1);
            }

            isResizing = false;
            if (isFunction(options.afterResize) && resizing) {
                options.afterResize.call(container, window.innerWidth, window.innerHeight);
            }
            if (isFunction(options.afterReBuild) && !resizing) {
                options.afterReBuild.call(container);
            }
        }

        /**
         * Turns fullPage.js to normal scrolling mode when the viewport `width` or `height`
         * are smaller than the set limit values.
         */
        function setResponsive(active) {
            var isResponsive = hasClass($body, RESPONSIVE);

            if (active) {
                if (!isResponsive) {
                    setAutoScrolling(false, 'internal');
                    setFitToSection(false, 'internal');
                    hide($(SECTION_NAV_SEL));
                    addClass($body, RESPONSIVE);
                    if (isFunction(options.afterResponsive)) {
                        options.afterResponsive.call(container, active);
                    }
                }
            } else if (isResponsive) {
                setAutoScrolling(originals.autoScrolling, 'internal');
                setFitToSection(originals.autoScrolling, 'internal');
                show($(SECTION_NAV_SEL));
                removeClass($body, RESPONSIVE);
                if (isFunction(options.afterResponsive)) {
                    options.afterResponsive.call(container, active);
                }
            }
        }

        if (container) {
            //public functions
            FP.version = '3.0.2';
            FP.setAutoScrolling = setAutoScrolling;
            FP.setRecordHistory = setRecordHistory;
            FP.setScrollingSpeed = setScrollingSpeed;
            FP.setFitToSection = setFitToSection;
            FP.setLockAnchors = setLockAnchors;
            FP.setMouseWheelScrolling = setMouseWheelScrolling;
            FP.setAllowScrolling = setAllowScrolling;
            FP.setKeyboardScrolling = setKeyboardScrolling;
            FP.moveSectionUp = moveSectionUp;
            FP.moveSectionDown = moveSectionDown;
            FP.silentMoveTo = silentMoveTo;
            FP.moveTo = moveTo;
            FP.moveSlideRight = moveSlideRight;
            FP.moveSlideLeft = moveSlideLeft;
            FP.fitToSection = fitToSection;
            FP.reBuild = reBuild;
            FP.setResponsive = setResponsive;
            FP.getFullpageData = options;
            FP.destroy = destroy;
            FP.getActiveSection = getActiveSection;
            FP.getActiveSlide = getActiveSlide;

            FP.test = {
                top: '0px',
                translate3d: 'translate3d(0px, 0px, 0px)',
                translate3dH: (function () {
                    var a = [];
                    for (var i = 0; i < $(options.sectionSelector, container).length; i++) {
                        a.push('translate3d(0px, 0px, 0px)');
                    }
                    return a;
                })(),
                left: (function () {
                    var a = [];
                    for (var i = 0; i < $(options.sectionSelector, container).length; i++) {
                        a.push(0);
                    }
                    return a;
                })(),
                options: options,
                setAutoScrolling: setAutoScrolling
            };

            //functions we want to share across files but which are not
            //mean to be used on their own by developers
            FP.shared = {
                afterRenderActions: afterRenderActions
            };

            window.fullpage_api = FP;

            init();

            bindEvents();
        }

        function init() {
            //if css3 is not supported, it will use jQuery animations
            if (options.css3) {
                options.css3 = support3d();
            }

            options.scrollBar = options.scrollBar || options.hybrid;

            setOptionsFromDOM();
            prepareDom();
            setAllowScrolling(true);
            setMouseHijack(true);
            setAutoScrolling(options.autoScrolling, 'internal');
            responsive();

            //setting the class for the body element
            setBodyClass();

            if (document.readyState === 'complete') {
                scrollToAnchor();
            }
            window.addEventListener('load', scrollToAnchor);
        }

        function bindEvents() {

            //when scrolling...
            window.addEventListener('scroll', scrollHandler);

            //detecting any change on the URL to scroll to the given anchor link
            //(a way to detect back history button as we play with the hashes on the URL)
            window.addEventListener('hashchange', hashChangeHandler);

            //when opening a new tab (ctrl + t), `control` won't be pressed when coming back.
            window.addEventListener('blur', blurHandler);

            //when resizing the site, we adjust the heights of the sections, slimScroll...
            window.addEventListener('resize', resizeHandler);

            //Sliding with arrow keys, both, vertical and horizontal
            document.addEventListener('keydown', keydownHandler);

            //to prevent scrolling while zooming
            document.addEventListener('keyup', keyUpHandler);

            //Scrolls to the section when clicking the navigation bullet
            //simulating the jQuery .on('click') event using delegation
            ['click', 'touchstart'].forEach(function (eventName) {
                document.addEventListener(eventName, delegatedEvents);
            });

            /**
             * Applying normalScroll elements.
             * Ignoring the scrolls over the specified selectors.
             */
            if (options.normalScrollElements) {
                ['mouseenter', 'touchstart'].forEach(function (eventName) {
                    forMouseLeaveOrTOuch(eventName, false);
                });

                ['mouseleave', 'touchend'].forEach(function (eventName) {
                    forMouseLeaveOrTOuch(eventName, true);
                });
            }
        }

        function delegatedEvents(e) {
            var target = e.target;

            if (target && closest(target, SECTION_NAV_SEL + ' a')) {
                sectionBulletHandler.call(target, e);
            } else if (matches(target, SECTION_NAV_TOOLTIP_SEL)) {
                tooltipTextHandler.call(target);
            } else if (matches(target, SLIDES_ARROW_SEL)) {
                slideArrowHandler.call(target, e);
            } else if (matches(target, SLIDES_NAV_LINK_SEL) || closest(target, SLIDES_NAV_LINK_SEL) != null) {
                slideBulletHandler.call(target, e);
            }
        }

        function forMouseLeaveOrTOuch(eventName, allowScrolling) {
            //a way to pass arguments to the onMouseEnterOrLeave function
            document['fp_' + eventName] = allowScrolling;
            document.addEventListener(eventName, onMouseEnterOrLeave, true); //capturing phase
        }

        function onMouseEnterOrLeave(e) {
            if (e.target == document) {
                return;
            }
            var normalSelectors = options.normalScrollElements.split(',');
            normalSelectors.forEach(function (normalSelector) {
                if (matches(e.target, normalSelector)) {
                    setMouseHijack(document['fp_' + e.type]); //e.type = eventName
                }
            });
        }

        /**
         * Setting options from DOM elements if they are not provided.
         */
        function setOptionsFromDOM() {

            //no anchors option? Checking for them in the DOM attributes
            if (!options.anchors.length) {
                var attrName = '[data-anchor]';
                var anchors = $(options.sectionSelector.split(',').join(attrName + ',') + attrName, container);
                if (anchors.length) {
                    anchors.forEach(function (item) {
                        options.anchors.push(item.getAttribute('data-anchor').toString());
                    });
                }
            }

            //no tooltips option? Checking for them in the DOM attributes
            if (!options.navigationTooltips.length) {
                var attrName = '[data-tooltip]';
                var tooltips = $(options.sectionSelector.split(',').join(attrName + ',') + attrName, container);
                if (tooltips.length) {
                    tooltips.forEach(function (item) {
                        options.navigationTooltips.push(item.getAttribute('data-tooltip').toString());
                    });
                }
            }
        }

        /**
         * Works over the DOM structure to set it up for the current fullpage options.
         */
        function prepareDom() {
            css(container, {
                'height': '100%',
                'position': 'relative'
            });

            //adding a class to recognize the container internally in the code
            addClass(container, WRAPPER);
            addClass($('html'), ENABLED);

            //due to https://github.com/alvarotrigo/fullPage.js/issues/1502
            windowsHeight = getWindowHeight();

            removeClass(container, DESTROYED); //in case it was destroyed before initializing it again

            addInternalSelectors();

            var sections = $(SECTION_SEL);

            //styling the sections / slides / menu
            for (var i = 0; i < sections.length; i++) {
                var sectionIndex = i;
                var section = sections[i];
                var slides = $(SLIDE_SEL, section);
                var numSlides = slides.length;

                //caching the original styles to add them back on destroy('all')
                section.setAttribute('data-fp-styles', section.getAttribute('style'));

                styleSection(section, sectionIndex);
                styleMenu(section, sectionIndex);

                // if there's any slide
                if (numSlides > 0) {
                    styleSlides(section, slides, numSlides);
                } else {
                    if (options.verticalCentered) {
                        addTableClass(section);
                    }
                }
            }

            //fixed elements need to be moved out of the plugin container due to problems with CSS3.
            if (options.fixedElements && options.css3) {
                $(options.fixedElements).forEach(function (item) {
                    $body.appendChild(item);
                });
            }

            //vertical centered of the navigation + active bullet
            if (options.navigation) {
                addVerticalNavigation();
            }

            enableYoutubeAPI();

            if (options.scrollOverflow) {
                scrollBarHandler = options.scrollOverflowHandler.init(options);
            } else {
                afterRenderActions();
            }
        }

        /**
         * Styles the horizontal slides for a section.
         */
        function styleSlides(section, slides, numSlides) {
            var sliderWidth = numSlides * 100;
            var slideWidth = 100 / numSlides;

            var slidesWrapper = document.createElement('div');
            slidesWrapper.className = SLIDES_WRAPPER; //fp-slides
            wrapAll(slides, slidesWrapper);

            var slidesContainer = document.createElement('div');
            slidesContainer.className = SLIDES_CONTAINER; //fp-slidesContainer
            wrapAll(slides, slidesContainer);

            css($(SLIDES_CONTAINER_SEL, section), {'width': sliderWidth + '%'});

            if (numSlides > 1) {
                if (options.controlArrows) {
                    createSlideArrows(section);
                }

                if (options.slidesNavigation) {
                    addSlidesNavigation(section, numSlides);
                }
            }

            slides.forEach(function (slide) {
                css(slide, {'width': slideWidth + '%'});

                if (options.verticalCentered) {
                    addTableClass(slide);
                }
            });

            var startingSlide = $(SLIDE_ACTIVE_SEL, section)[0];

            //if the slide won't be an starting point, the default will be the first one
            //the active section isn't the first one? Is not the first slide of the first section? Then we load that section/slide by default.
            if (startingSlide != null && (index($(SECTION_ACTIVE_SEL), SECTION_SEL) !== 0 || (index($(SECTION_ACTIVE_SEL), SECTION_SEL) === 0 && index(startingSlide) !== 0))) {
                silentLandscapeScroll(startingSlide, 'internal');
            } else {
                addClass(slides[0], ACTIVE);
            }
        }

        /**
         * Styling vertical sections
         */
        function styleSection(section, index) {
            //if no active section is defined, the 1st one will be the default one
            if (!index && $(SECTION_ACTIVE_SEL)[0] == null) {
                addClass(section, ACTIVE);
            }
            startingSection = $(SECTION_ACTIVE_SEL)[0];

            css(section, {'height': windowsHeight + 'px'});

            if (options.paddingTop) {
                css(section, {'padding-top': options.paddingTop});
            }

            if (options.paddingBottom) {
                css(section, {'padding-bottom': options.paddingBottom});
            }

            if (typeof options.sectionsColor[index] !== 'undefined') {
                css(section, {'background-color': options.sectionsColor[index]});
            }

            if (typeof options.anchors[index] !== 'undefined') {
                section.setAttribute('data-anchor', options.anchors[index]);
            }
        }

        /**
         * Sets the data-anchor attributes to the menu elements and activates the current one.
         */
        function styleMenu(section, index) {
            if (typeof options.anchors[index] !== 'undefined') {
                //activating the menu / nav element on load
                if (hasClass(section, ACTIVE)) {
                    activateMenuAndNav(options.anchors[index], index);
                }
            }

            //moving the menu outside the main container if it is inside (avoid problems with fixed positions when using CSS3 tranforms)
            if (options.menu && options.css3 && closest($(options.menu)[0], WRAPPER_SEL) != null) {
                $body.appendChild($(options.menu)[0]);
            }
        }

        /**
         * Adds internal classes to be able to provide customizable selectors
         * keeping the link with the style sheet.
         */
        function addInternalSelectors() {
            addClass($(options.sectionSelector, container), SECTION);
            addClass($(options.slideSelector, container), SLIDE);
        }

        /**
         * Creates the control arrows for the given section
         */
        function createSlideArrows(section) {
            var arrows = [createElementFromHTML('<div class="' + SLIDES_ARROW_PREV + '"></div>'), createElementFromHTML('<div class="' + SLIDES_ARROW_NEXT + '"></div>')];
            after($(SLIDES_WRAPPER_SEL, section)[0], arrows);

            if (options.controlArrowColor !== '#fff') {
                css($(SLIDES_ARROW_NEXT_SEL, section), {'border-color': 'transparent transparent transparent ' + options.controlArrowColor});
                css($(SLIDES_ARROW_PREV_SEL, section), {'border-color': 'transparent ' + options.controlArrowColor + ' transparent transparent'});
            }

            if (!options.loopHorizontal) {
                hide($(SLIDES_ARROW_PREV_SEL, section));
            }
        }

        /**
         * Creates a vertical navigation bar.
         */
        function addVerticalNavigation() {
            var navigation = document.createElement('div');
            navigation.setAttribute('id', SECTION_NAV);

            var divUl = document.createElement('ul');
            navigation.appendChild(divUl);

            appendTo(navigation, $body);
            var nav = $(SECTION_NAV_SEL)[0];

            addClass(nav, 'fp-' + options.navigationPosition);

            if (options.showActiveTooltip) {
                addClass(nav, SHOW_ACTIVE_TOOLTIP);
            }

            var li = '';

            for (var i = 0; i < $(SECTION_SEL).length; i++) {
                var link = '';
                if (options.anchors.length) {
                    link = options.anchors[i];
                }

                li += '<li><a href="#' + link + '"><span class="fp-sr-only">' + getBulletLinkName(i, 'Section') + '</span><span></span></a>';

                // Only add tooltip if needed (defined by user)
                var tooltip = options.navigationTooltips[i];

                if (typeof tooltip !== 'undefined' && tooltip !== '') {
                    li += '<div class="' + SECTION_NAV_TOOLTIP + ' fp-' + options.navigationPosition + '">' + tooltip + '</div>';
                }

                li += '</li>';
            }
            $('ul', nav)[0].innerHTML = li;

            //centering it vertically
            css($(SECTION_NAV_SEL), {'margin-top': '-' + ($(SECTION_NAV_SEL)[0].offsetHeight / 2) + 'px'});

            //activating the current active section

            var bullet = $('li', $(SECTION_NAV_SEL)[0])[index($(SECTION_ACTIVE_SEL)[0], SECTION_SEL)];
            addClass($('a', bullet), ACTIVE);
        }

        /**
         * Gets the name for screen readers for a section/slide navigation bullet.
         */
        function getBulletLinkName(i, defaultName) {
            return options.navigationTooltips[i]
                || options.anchors[i]
                || defaultName + ' ' + (i + 1)
        }

        /*
        * Enables the Youtube videos API so we can control their flow if necessary.
        */
        function enableYoutubeAPI() {
            $('iframe[src*="youtube.com/embed/"]', container).forEach(function (item) {
                addURLParam(item, 'enablejsapi=1');
            });
        }

        /**
         * Adds a new parameter and its value to the `src` of a given element
         */
        function addURLParam(element, newParam) {
            var originalSrc = element.getAttribute('src');
            element.setAttribute('src', originalSrc + getUrlParamSign(originalSrc) + newParam);
        }

        /*
        * Returns the prefix sign to use for a new parameter in an existen URL.
        *
        * @return {String}  ? | &
        */
        function getUrlParamSign(url) {
            return (!/\?/.test(url)) ? '?' : '&';
        }

        /**
         * Actions and callbacks to fire afterRender
         */
        function afterRenderActions() {
            var section = $(SECTION_ACTIVE_SEL)[0];

            addClass(section, COMPLETELY);

            lazyLoad(section);
            playMedia(section);

            if (options.scrollOverflow) {
                options.scrollOverflowHandler.afterLoad();
            }

            if (isDestinyTheStartingSection() && isFunction(options.afterLoad)) {
                fireCallback('afterLoad', {
                    activeSection: null,
                    element: section,
                    direction: null,

                    //for backwards compatibility callback (to be removed in a future!)
                    anchorLink: section.getAttribute('data-anchor'),
                    sectionIndex: index(section, SECTION_SEL)
                });
            }

            if (isFunction(options.afterRender)) {
                fireCallback('afterRender');
            }
        }

        /**
         * Determines if the URL anchor destiny is the starting section (the one using 'active' class before initialization)
         */
        function isDestinyTheStartingSection() {
            var destinationSection = getSectionByAnchor(getAnchorsURL().section);
            return !destinationSection || typeof destinationSection !== 'undefined' && index(destinationSection) === index(startingSection);
        }

        var isScrolling = false;
        var lastScroll = 0;

        //when scrolling...
        function scrollHandler() {
            var currentSection;

            if (!options.autoScrolling || options.scrollBar) {
                var currentScroll = getScrollTop();
                var scrollDirection = getScrollDirection(currentScroll);
                var visibleSectionIndex = 0;
                var screen_mid = currentScroll + (getWindowHeight() / 2.0);
                var isAtBottom = $body.offsetHeight - getWindowHeight() === currentScroll;
                var sections = $(SECTION_SEL);

                //when using `auto-height` for a small last section it won't be centered in the viewport
                if (isAtBottom) {
                    visibleSectionIndex = sections.length - 1;
                }
                //is at top? when using `auto-height` for a small first section it won't be centered in the viewport
                else if (!currentScroll) {
                    visibleSectionIndex = 0;
                }

                //taking the section which is showing more content in the viewport
                else {
                    for (var i = 0; i < sections.length; ++i) {
                        var section = sections[i];

                        // Pick the the last section which passes the middle line of the screen.
                        if (section.offsetTop <= screen_mid) {
                            visibleSectionIndex = i;
                        }
                    }
                }

                if (isCompletelyInViewPort(scrollDirection)) {
                    if (!hasClass($(SECTION_ACTIVE_SEL)[0], COMPLETELY)) {
                        addClass($(SECTION_ACTIVE_SEL)[0], COMPLETELY);
                        removeClass(siblings($(SECTION_ACTIVE_SEL)[0]), COMPLETELY);
                    }
                }

                //geting the last one, the current one on the screen
                currentSection = sections[visibleSectionIndex];

                //setting the visible section as active when manually scrolling
                //executing only once the first time we reach the section
                if (!hasClass(currentSection, ACTIVE)) {
                    isScrolling = true;
                    var leavingSection = $(SECTION_ACTIVE_SEL)[0];
                    var leavingSectionIndex = index(leavingSection, SECTION_SEL) + 1;
                    var yMovement = getYmovement(currentSection);
                    var anchorLink = currentSection.getAttribute('data-anchor');
                    var sectionIndex = index(currentSection, SECTION_SEL) + 1;
                    var activeSlide = $(SLIDE_ACTIVE_SEL, currentSection)[0];
                    var slideIndex;
                    var slideAnchorLink;
                    var callbacksParams = {
                        activeSection: leavingSection,
                        sectionIndex: sectionIndex - 1,
                        anchorLink: anchorLink,
                        element: currentSection,
                        leavingSection: leavingSectionIndex,
                        direction: yMovement
                    };

                    if (activeSlide) {
                        slideAnchorLink = activeSlide.getAttribute('data-anchor');
                        slideIndex = index(activeSlide);
                    }

                    if (canScroll) {
                        addClass(currentSection, ACTIVE);
                        removeClass(siblings(currentSection), ACTIVE);

                        if (isFunction(options.onLeave)) {
                            fireCallback('onLeave', callbacksParams);
                        }
                        if (isFunction(options.afterLoad)) {
                            fireCallback('afterLoad', callbacksParams);
                        }

                        stopMedia(leavingSection);
                        lazyLoad(currentSection);
                        playMedia(currentSection);

                        activateMenuAndNav(anchorLink, sectionIndex - 1);

                        if (options.anchors.length) {
                            //needed to enter in hashChange event when using the menu with anchor links
                            lastScrolledDestiny = anchorLink;
                        }
                        setState(slideIndex, slideAnchorLink, anchorLink, sectionIndex);
                    }

                    //small timeout in order to avoid entering in hashChange event when scrolling is not finished yet
                    clearTimeout(scrollId);
                    scrollId = setTimeout(function () {
                        isScrolling = false;
                    }, 100);
                }

                if (options.fitToSection) {
                    //for the auto adjust of the viewport to fit a whole section
                    clearTimeout(scrollId2);

                    scrollId2 = setTimeout(function () {
                        //checking it again in case it changed during the delay
                        if (options.fitToSection &&

                            //is the destination element bigger than the viewport?
                            $(SECTION_ACTIVE_SEL)[0].offsetHeight <= windowsHeight
                        ) {
                            fitToSection();
                        }
                    }, options.fitToSectionDelay);
                }
            }
        }

        /**
         * Fits the site to the nearest active section
         */
        function fitToSection() {
            //checking fitToSection again in case it was set to false before the timeout delay
            if (canScroll) {
                //allows to scroll to an active section and
                //if the section is already active, we prevent firing callbacks
                isResizing = true;

                scrollPage($(SECTION_ACTIVE_SEL)[0]);
                isResizing = false;
            }
        }

        /**
         * Determines whether the active section has seen in its whole or not.
         */
        function isCompletelyInViewPort(movement) {
            var top = $(SECTION_ACTIVE_SEL)[0].offsetTop;
            var bottom = top + getWindowHeight();

            if (movement == 'up') {
                return bottom >= (getScrollTop() + getWindowHeight());
            }
            return top <= getScrollTop();
        }

        /**
         * Gets the directon of the the scrolling fired by the scroll event.
         */
        function getScrollDirection(currentScroll) {
            var direction = currentScroll > lastScroll ? 'down' : 'up';

            lastScroll = currentScroll;

            //needed for auto-height sections to determine if we want to scroll to the top or bottom of the destination
            previousDestTop = currentScroll;

            return direction;
        }

        /**
         * Determines the way of scrolling up or down:
         * by 'automatically' scrolling a section or by using the default and normal scrolling.
         */
        function scrolling(type) {
            if (!isScrollAllowed.m[type]) {
                return;
            }

            var scrollSection = (type === 'down') ? moveSectionDown : moveSectionUp;

            if (options.scrollOverflow) {
                var scrollable = options.scrollOverflowHandler.scrollable($(SECTION_ACTIVE_SEL)[0]);
                var check = (type === 'down') ? 'bottom' : 'top';

                if (scrollable != null) {
                    //is the scrollbar at the start/end of the scroll?
                    if (options.scrollOverflowHandler.isScrolled(check, scrollable)) {
                        scrollSection();
                    } else {
                        return true;
                    }
                } else {
                    // moved up/down
                    scrollSection();
                }
            } else {
                // moved up/down
                scrollSection();
            }
        }

        /*
        * Preventing bouncing in iOS #2285
        */
        function preventBouncing(e) {
            if (options.autoScrolling && isReallyTouch(e)) {
                //preventing the easing on iOS devices
                preventDefault(e);
            }
        }

        var touchStartY = 0;
        var touchStartX = 0;
        var touchEndY = 0;
        var touchEndX = 0;

        /* Detecting touch events

        * As we are changing the top property of the page on scrolling, we can not use the traditional way to detect it.
        * This way, the touchstart and the touch moves shows an small difference between them which is the
        * used one to determine the direction.
        */
        function touchMoveHandler(e) {
            var activeSection = closest(e.target, SECTION_SEL);

            // additional: if one of the normalScrollElements isn't within options.normalScrollElementTouchThreshold hops up the DOM chain
            if (isReallyTouch(e)) {

                if (options.autoScrolling) {
                    //preventing the easing on iOS devices
                    preventDefault(e);
                }

                var touchEvents = getEventsPage(e);

                touchEndY = touchEvents.y;
                touchEndX = touchEvents.x;

                //if movement in the X axys is greater than in the Y and the currect section has slides...
                if ($(SLIDES_WRAPPER_SEL, activeSection).length && Math.abs(touchStartX - touchEndX) > (Math.abs(touchStartY - touchEndY))) {

                    //is the movement greater than the minimum resistance to scroll?
                    if (!slideMoving && Math.abs(touchStartX - touchEndX) > (window.innerWidth / 100 * options.touchSensitivity)) {
                        if (touchStartX > touchEndX) {
                            if (isScrollAllowed.m.right) {
                                moveSlideRight(activeSection); //next
                            }
                        } else {
                            if (isScrollAllowed.m.left) {
                                moveSlideLeft(activeSection); //prev
                            }
                        }
                    }
                }

                //vertical scrolling (only when autoScrolling is enabled)
                else if (options.autoScrolling && canScroll) {

                    //is the movement greater than the minimum resistance to scroll?
                    if (Math.abs(touchStartY - touchEndY) > (window.innerHeight / 100 * options.touchSensitivity)) {
                        if (touchStartY > touchEndY) {
                            scrolling('down');
                        } else if (touchEndY > touchStartY) {
                            scrolling('up');
                        }
                    }
                }
            }
        }

        /**
         * As IE >= 10 fires both touch and mouse events when using a mouse in a touchscreen
         * this way we make sure that is really a touch event what IE is detecting.
         */
        function isReallyTouch(e) {
            //if is not IE   ||  IE is detecting `touch` or `pen`
            return typeof e.pointerType === 'undefined' || e.pointerType != 'mouse';
        }

        /**
         * Handler for the touch start event.
         */
        function touchStartHandler(e) {

            //stopping the auto scroll to adjust to a section
            if (options.fitToSection) {
                activeAnimation = false;
            }

            if (isReallyTouch(e)) {
                var touchEvents = getEventsPage(e);
                touchStartY = touchEvents.y;
                touchStartX = touchEvents.x;
            }
        }

        /**
         * Gets the average of the last `number` elements of the given array.
         */
        function getAverage(elements, number) {
            var sum = 0;

            //taking `number` elements from the end to make the average, if there are not enought, 1
            var lastElements = elements.slice(Math.max(elements.length - number, 1));

            for (var i = 0; i < lastElements.length; i++) {
                sum = sum + lastElements[i];
            }

            return Math.ceil(sum / number);
        }

        /**
         * Detecting mousewheel scrolling
         *
         * http://blogs.sitepointstatic.com/examples/tech/mouse-wheel/index.html
         * http://www.sitepoint.com/html5-javascript-mouse-wheel/
         */
        var prevTime = new Date().getTime();

        function MouseWheelHandler(e) {
            var curTime = new Date().getTime();
            var isNormalScroll = hasClass($(COMPLETELY_SEL)[0], NORMAL_SCROLL);

            //is scroll allowed?
            if (!isScrollAllowed.m.down && !isScrollAllowed.m.up) {
                preventDefault(e);
                return false;
            }

            //autoscrolling and not zooming?
            if (options.autoScrolling && !controlPressed && !isNormalScroll) {
                // cross-browser wheel delta
                e = e || window.event;
                var value = e.wheelDelta || -e.deltaY || -e.detail;
                var delta = Math.max(-1, Math.min(1, value));

                var horizontalDetection = typeof e.wheelDeltaX !== 'undefined' || typeof e.deltaX !== 'undefined';
                var isScrollingVertically = (Math.abs(e.wheelDeltaX) < Math.abs(e.wheelDelta)) || (Math.abs(e.deltaX) < Math.abs(e.deltaY) || !horizontalDetection);

                //Limiting the array to 150 (lets not waste memory!)
                if (scrollings.length > 149) {
                    scrollings.shift();
                }

                //keeping record of the previous scrollings
                scrollings.push(Math.abs(value));

                //preventing to scroll the site on mouse wheel when scrollbar is present
                if (options.scrollBar) {
                    preventDefault(e);
                }

                //time difference between the last scroll and the current one
                var timeDiff = curTime - prevTime;
                prevTime = curTime;

                //haven't they scrolled in a while?
                //(enough to be consider a different scrolling action to scroll another section)
                if (timeDiff > 200) {
                    //emptying the array, we dont care about old scrollings for our averages
                    scrollings = [];
                }

                if (canScroll) {
                    var averageEnd = getAverage(scrollings, 10);
                    var averageMiddle = getAverage(scrollings, 70);
                    var isAccelerating = averageEnd >= averageMiddle;

                    //to avoid double swipes...
                    if (isAccelerating && isScrollingVertically) {
                        //scrolling down?
                        if (delta < 0) {
                            scrolling('down');

                            //scrolling up?
                        } else {
                            scrolling('up');
                        }
                    }
                }

                return false;
            }

            if (options.fitToSection) {
                //stopping the auto scroll to adjust to a section
                activeAnimation = false;
            }
        }

        /**
         * Slides a slider to the given direction.
         * Optional `section` param.
         */
        function moveSlide(direction, section) {
            var activeSection = section == null ? $(SECTION_ACTIVE_SEL)[0] : section;
            var slides = $(SLIDES_WRAPPER_SEL, activeSection)[0];

            // more than one slide needed and nothing should be sliding
            if (slides == null || slideMoving || $(SLIDE_SEL, slides).length < 2) {
                return;
            }

            var currentSlide = $(SLIDE_ACTIVE_SEL, slides)[0];
            var destiny = null;

            if (direction === 'left') {
                destiny = prevUntil(currentSlide, SLIDE_SEL);
            } else {
                destiny = nextUntil(currentSlide, SLIDE_SEL);
            }

            //isn't there a next slide in the secuence?
            if (destiny == null) {
                //respect loopHorizontal settin
                if (!options.loopHorizontal) return;

                var slideSiblings = siblings(currentSlide);
                if (direction === 'left') {
                    destiny = slideSiblings[slideSiblings.length - 1]; //last
                } else {
                    destiny = slideSiblings[0]; //first
                }
            }

            slideMoving = true && !FP.test.isTesting;
            landscapeScroll(slides, destiny, direction);
        }

        /**
         * Maintains the active slides in the viewport
         * (Because the `scroll` animation might get lost with some actions, such as when using continuousVertical)
         */
        function keepSlidesPosition() {
            var activeSlides = $(SLIDE_ACTIVE_SEL);
            for (var i = 0; i < activeSlides.length; i++) {
                silentLandscapeScroll(activeSlides[i], 'internal');
            }
        }

        var previousDestTop = 0;

        /**
         * Returns the destination Y position based on the scrolling direction and
         * the height of the section.
         */
        function getDestinationPosition(element) {
            var elementHeight = element.offsetHeight;
            var elementTop = element.offsetTop;

            //top of the desination will be at the top of the viewport
            var position = elementTop;
            var isScrollingDown = elementTop > previousDestTop;
            var sectionBottom = position - windowsHeight + elementHeight;
            var bigSectionsDestination = options.bigSectionsDestination;

            //is the destination element bigger than the viewport?
            if (elementHeight > windowsHeight) {
                //scrolling up?
                if (!isScrollingDown && !bigSectionsDestination || bigSectionsDestination === 'bottom') {
                    position = sectionBottom;
                }
            }

            //sections equal or smaller than the viewport height && scrolling down? ||  is resizing and its in the last section
            else if (isScrollingDown || (isResizing && next(element) == null)) {
                //The bottom of the destination will be at the bottom of the viewport
                position = sectionBottom;
            }

            /*
            Keeping record of the last scrolled position to determine the scrolling direction.
            No conventional methods can be used as the scroll bar might not be present
            AND the section might not be active if it is auto-height and didnt reach the middle
            of the viewport.
            */
            previousDestTop = position;
            return position;
        }

        /**
         * Scrolls the site to the given element and scrolls to the slide if a callback is given.
         */
        function scrollPage(element, callback, isMovementUp) {
            if (element == null) {
                return;
            } //there's no element to scroll, leaving the function

            var dtop = getDestinationPosition(element);
            var slideAnchorLink;
            var slideIndex;

            //local variables
            var v = {
                element: element,
                callback: callback,
                isMovementUp: isMovementUp,
                dtop: dtop,
                yMovement: getYmovement(element),
                anchorLink: element.getAttribute('data-anchor'),
                sectionIndex: index(element, SECTION_SEL),
                activeSlide: $(SLIDE_ACTIVE_SEL, element)[0],
                activeSection: $(SECTION_ACTIVE_SEL)[0],
                leavingSection: index($(SECTION_ACTIVE_SEL), SECTION_SEL) + 1,

                //caching the value of isResizing at the momment the function is called
                //because it will be checked later inside a setTimeout and the value might change
                localIsResizing: isResizing
            };

            //quiting when destination scroll is the same as the current one
            if ((v.activeSection == element && !isResizing) || (options.scrollBar && getScrollTop() === v.dtop && !hasClass(element, AUTO_HEIGHT))) {
                return;
            }

            if (v.activeSlide != null) {
                slideAnchorLink = v.activeSlide.getAttribute('data-anchor');
                slideIndex = index(v.activeSlide);
            }

            //callback (onLeave) if the site is not just resizing and readjusting the slides
            if (isFunction(options.onLeave) && !v.localIsResizing) {
                var direction = v.yMovement;

                //required for continousVertical
                if (typeof isMovementUp !== 'undefined') {
                    direction = isMovementUp ? 'up' : 'down';
                }

                //for the callback
                v.direction = direction;

                if (fireCallback('onLeave', v) === false) {
                    return;
                }
            }

            // If continuousVertical && we need to wrap around
            if (options.autoScrolling && options.continuousVertical && typeof (v.isMovementUp) !== "undefined" &&
                ((!v.isMovementUp && v.yMovement == 'up') || // Intending to scroll down but about to go up or
                    (v.isMovementUp && v.yMovement == 'down'))) { // intending to scroll up but about to go down

                v = createInfiniteSections(v);
            }

            //pausing media of the leaving section (if we are not just resizing, as destinatino will be the same one)
            if (!v.localIsResizing) {
                stopMedia(v.activeSection);
            }

            if (options.scrollOverflow) {
                options.scrollOverflowHandler.beforeLeave();
            }

            addClass(element, ACTIVE);
            removeClass(siblings(element), ACTIVE);
            lazyLoad(element);

            if (options.scrollOverflow) {
                options.scrollOverflowHandler.onLeave();
            }

            //preventing from activating the MouseWheelHandler event
            //more than once if the page is scrolling
            canScroll = false || FP.test.isTesting;

            setState(slideIndex, slideAnchorLink, v.anchorLink, v.sectionIndex);

            performMovement(v);

            //flag to avoid callingn `scrollPage()` twice in case of using anchor links
            lastScrolledDestiny = v.anchorLink;

            //avoid firing it twice (as it does also on scroll)
            activateMenuAndNav(v.anchorLink, v.sectionIndex);
        }

        /**
         * Dispatch events & callbacks making sure it does it on the right format, depending on
         * whether v2compatible is being used or not.
         */
        function fireCallback(eventName, v) {
            var eventData = getEventData(eventName, v);

            if (!options.v2compatible) {
                trigger(container, eventName, eventData);

                if (options[eventName].apply(eventData[Object.keys(eventData)[0]], toArray(eventData)) === false) {
                    return false;
                }
            } else {
                if (options[eventName].apply(eventData[0], eventData.slice(1)) === false) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Makes sure to only create a Panel object if the element exist
         */
        function nullOrSection(el) {
            return el ? new Section(el) : null;
        }

        function nullOrSlide(el) {
            return el ? new Slide(el) : null;
        }

        /**
         * Gets the event's data for the given event on the right format. Depending on whether
         * v2compatible is being used or not.
         */
        function getEventData(eventName, v) {
            var paramsPerEvent;

            if (!options.v2compatible) {

                //using functions to run only the necessary bits within the object
                paramsPerEvent = {
                    afterRender: function () {
                        return {
                            section: nullOrSection($(SECTION_ACTIVE_SEL)[0]),
                            slide: nullOrSlide($(SLIDE_ACTIVE_SEL, $(SECTION_ACTIVE_SEL)[0])[0])
                        };
                    },
                    onLeave: function () {
                        return {
                            origin: nullOrSection(v.activeSection),
                            destination: nullOrSection(v.element),
                            direction: v.direction
                        };
                    },

                    afterLoad: function () {
                        return paramsPerEvent.onLeave();
                    },

                    afterSlideLoad: function () {
                        return {
                            section: nullOrSection(v.section),
                            origin: nullOrSlide(v.prevSlide),
                            destination: nullOrSlide(v.destiny),
                            direction: v.direction
                        };
                    },

                    onSlideLeave: function () {
                        return paramsPerEvent.afterSlideLoad();
                    }
                };
            } else {
                paramsPerEvent = {
                    afterRender: function () {
                        return [container];
                    },
                    onLeave: function () {
                        return [v.activeSection, v.leavingSection, (v.sectionIndex + 1), v.direction];
                    },
                    afterLoad: function () {
                        return [v.element, v.anchorLink, (v.sectionIndex + 1)];
                    },
                    afterSlideLoad: function () {
                        return [v.destiny, v.anchorLink, (v.sectionIndex + 1), v.slideAnchor, v.slideIndex];
                    },
                    onSlideLeave: function () {
                        return [v.prevSlide, v.anchorLink, (v.sectionIndex + 1), v.prevSlideIndex, v.direction, v.slideIndex];
                    },
                };
            }

            return paramsPerEvent[eventName]();
        }

        /**
         * Performs the vertical movement (by CSS3 or by jQuery)
         */
        function performMovement(v) {
            // using CSS3 translate functionality
            if (options.css3 && options.autoScrolling && !options.scrollBar) {

                // The first section can have a negative value in iOS 10. Not quite sure why: -0.0142822265625
                // that's why we round it to 0.
                var translate3d = 'translate3d(0px, -' + Math.round(v.dtop) + 'px, 0px)';
                transformContainer(translate3d, true);

                //even when the scrollingSpeed is 0 there's a little delay, which might cause the
                //scrollingSpeed to change in case of using silentMoveTo();
                if (options.scrollingSpeed) {
                    clearTimeout(afterSectionLoadsId);
                    afterSectionLoadsId = setTimeout(function () {
                        afterSectionLoads(v);
                    }, options.scrollingSpeed);
                } else {
                    afterSectionLoads(v);
                }
            }

            // using JS to animate
            else {
                var scrollSettings = getScrollSettings(v.dtop);
                FP.test.top = -v.dtop + 'px';

                scrollTo(scrollSettings.element, scrollSettings.options, options.scrollingSpeed, function () {
                    if (options.scrollBar) {

                        /* Hack!
                        The timeout prevents setting the most dominant section in the viewport as "active" when the user
                        scrolled to a smaller section by using the mousewheel (auto scrolling) rather than draging the scroll bar.

                        When using scrollBar:true It seems like the scroll events still getting propagated even after the scrolling animation has finished.
                        */
                        setTimeout(function () {
                            afterSectionLoads(v);
                        }, 30);
                    } else {
                        afterSectionLoads(v);
                    }
                });
            }
        }

        /**
         * Gets the scrolling settings depending on the plugin autoScrolling option
         */
        function getScrollSettings(top) {
            var scroll = {};

            //top property animation
            if (options.autoScrolling && !options.scrollBar) {
                scroll.options = -top;
                scroll.element = $(WRAPPER_SEL)[0];
            }

            //window real scrolling
            else {
                scroll.options = top;
                scroll.element = window;
            }

            return scroll;
        }

        /**
         * Adds sections before or after the current one to create the infinite effect.
         */
        function createInfiniteSections(v) {
            // Scrolling down
            if (!v.isMovementUp) {
                // Move all previous sections to after the active section
                after($(SECTION_ACTIVE_SEL)[0], prevAll(v.activeSection, SECTION_SEL).reverse());
            } else { // Scrolling up
                // Move all next sections to before the active section
                before($(SECTION_ACTIVE_SEL)[0], nextAll(v.activeSection, SECTION_SEL));
            }

            // Maintain the displayed position (now that we changed the element order)
            silentScroll($(SECTION_ACTIVE_SEL)[0].offsetTop);

            // Maintain the active slides visible in the viewport
            keepSlidesPosition();

            // save for later the elements that still need to be reordered
            v.wrapAroundElements = v.activeSection;

            // Recalculate animation variables
            v.dtop = v.element.offsetTop;
            v.yMovement = getYmovement(v.element);

            //sections will temporally have another position in the DOM
            //updating this values in case we need them
            v.leavingSection = index(v.activeSection, SECTION_SEL) + 1;
            v.sectionIndex = index(v.element, SECTION_SEL);

            return v;
        }

        /**
         * Fix section order after continuousVertical changes have been animated
         */
        function continuousVerticalFixSectionOrder(v) {
            // If continuousVertical is in effect (and autoScrolling would also be in effect then),
            // finish moving the elements around so the direct navigation will function more simply
            if (v.wrapAroundElements == null) {
                return;
            }

            if (v.isMovementUp) {
                before($(SECTION_SEL)[0], v.wrapAroundElements);
            } else {
                after($(SECTION_SEL)[$(SECTION_SEL).length - 1], v.wrapAroundElements);
            }

            silentScroll($(SECTION_ACTIVE_SEL)[0].offsetTop);

            // Maintain the active slides visible in the viewport
            keepSlidesPosition();
        }


        /**
         * Actions to do once the section is loaded.
         */
        function afterSectionLoads(v) {
            continuousVerticalFixSectionOrder(v);

            //callback (afterLoad) if the site is not just resizing and readjusting the slides
            if (isFunction(options.afterLoad) && !v.localIsResizing) {
                fireCallback('afterLoad', v);
            }

            if (options.scrollOverflow) {
                options.scrollOverflowHandler.afterLoad();
            }

            if (!v.localIsResizing) {
                playMedia(v.element);
            }

            addClass(v.element, COMPLETELY);
            removeClass(siblings(v.element), COMPLETELY);

            canScroll = true;

            if (isFunction(v.callback)) {
                v.callback();
            }
        }

        /**
         * Sets the value for the given attribute from the `data-` attribute with the same suffix
         * ie: data-srcset ==> srcset  |  data-src ==> src
         */
        function setSrc(element, attribute) {
            element.setAttribute(attribute, element.getAttribute('data-' + attribute));
            element.removeAttribute('data-' + attribute);
        }

        /**
         * Lazy loads image, video and audio elements.
         */
        function lazyLoad(destiny) {
            if (!options.lazyLoading) {
                return;
            }

            var panel = getSlideOrSection(destiny);

            $('img[data-src], img[data-srcset], source[data-src], source[data-srcset], video[data-src], audio[data-src], iframe[data-src]', panel).forEach(function (element) {
                ['src', 'srcset'].forEach(function (type) {
                    var attribute = element.getAttribute('data-' + type);
                    if (attribute != null && attribute) {
                        setSrc(element, type);
                    }
                });

                if (matches(element, 'source')) {
                    var elementToPlay = closest(element, 'video, audio');
                    if (elementToPlay) {
                        elementToPlay.load();
                    }
                }
            });
        }

        /**
         * Plays video and audio elements.
         */
        function playMedia(destiny) {
            var panel = getSlideOrSection(destiny);

            //playing HTML5 media elements
            $('video, audio', panel).forEach(function (element) {
                if (element.hasAttribute('data-autoplay') && typeof element.play === 'function') {
                    element.play();
                }
            });

            //youtube videos
            $('iframe[src*="youtube.com/embed/"]', panel).forEach(function (element) {
                if (element.hasAttribute('data-autoplay')) {
                    playYoutube(element);
                }

                //in case the URL was not loaded yet. On page load we need time for the new URL (with the API string) to load.
                element.onload = function () {
                    if (element.hasAttribute('data-autoplay')) {
                        playYoutube(element);
                    }
                };
            });
        }

        /**
         * Plays a youtube video
         */
        function playYoutube(element) {
            element.contentWindow.postMessage('{"event":"command","func":"playVideo","args":""}', '*');
        }

        /**
         * Stops video and audio elements.
         */
        function stopMedia(destiny) {
            var panel = getSlideOrSection(destiny);

            //stopping HTML5 media elements
            $('video, audio', panel).forEach(function (element) {
                if (!element.hasAttribute('data-keepplaying') && typeof element.pause === 'function') {
                    element.pause();
                }
            });

            //youtube videos
            $('iframe[src*="youtube.com/embed/"]', panel).forEach(function (element) {
                if (/youtube\.com\/embed\//.test(element.getAttribute('src')) && !element.hasAttribute('data-keepplaying')) {
                    element.contentWindow.postMessage('{"event":"command","func":"pauseVideo","args":""}', '*');
                }
            });
        }

        /**
         * Gets the active slide (or section) for the given section
         */
        function getSlideOrSection(destiny) {
            var slide = $(SLIDE_ACTIVE_SEL, destiny);
            if (slide.length) {
                destiny = slide[0];
            }

            return destiny;
        }

        /**
         * Scrolls to the anchor in the URL when loading the site
         */
        function scrollToAnchor() {
            var anchors = getAnchorsURL();
            var sectionAnchor = anchors.section;
            var slideAnchor = anchors.slide;

            if (sectionAnchor) {  //if theres any #
                if (options.animateAnchor) {
                    scrollPageAndSlide(sectionAnchor, slideAnchor);
                } else {
                    silentMoveTo(sectionAnchor, slideAnchor);
                }
            }
        }

        /**
         * Detecting any change on the URL to scroll to the given anchor link
         * (a way to detect back history button as we play with the hashes on the URL)
         */
        function hashChangeHandler() {
            if (!isScrolling && !options.lockAnchors) {
                var anchors = getAnchorsURL();
                var sectionAnchor = anchors.section;
                var slideAnchor = anchors.slide;

                //when moving to a slide in the first section for the first time (first time to add an anchor to the URL)
                var isFirstSlideMove = (typeof lastScrolledDestiny === 'undefined');
                var isFirstScrollMove = (typeof lastScrolledDestiny === 'undefined' && typeof slideAnchor === 'undefined' && !slideMoving);

                if (sectionAnchor && sectionAnchor.length) {
                    /*in order to call scrollpage() only once for each destination at a time
                    It is called twice for each scroll otherwise, as in case of using anchorlinks `hashChange`
                    event is fired on every scroll too.*/
                    if ((sectionAnchor && sectionAnchor !== lastScrolledDestiny) && !isFirstSlideMove
                        || isFirstScrollMove
                        || (!slideMoving && lastScrolledSlide != slideAnchor)) {

                        scrollPageAndSlide(sectionAnchor, slideAnchor);
                    }
                }
            }
        }

        //gets the URL anchors (section and slide)
        function getAnchorsURL() {
            var section;
            var slide;
            var hash = window.location.hash;

            if (hash.length) {
                //getting the anchor link in the URL and deleting the `#`
                var anchorsParts = hash.replace('#', '').split('/');

                //using / for visual reasons and not as a section/slide separator #2803
                var isFunkyAnchor = hash.indexOf('#/') > -1;

                section = isFunkyAnchor ? '/' + anchorsParts[1] : decodeURIComponent(anchorsParts[0]);

                var slideAnchor = isFunkyAnchor ? anchorsParts[2] : anchorsParts[1];
                if (slideAnchor && slideAnchor.length) {
                    slide = decodeURIComponent(slideAnchor);
                }
            }

            return {
                section: section,
                slide: slide
            };
        }

        //Sliding with arrow keys, both, vertical and horizontal
        function keydownHandler(e) {
            clearTimeout(keydownId);

            var activeElement = document.activeElement;
            var keyCode = e.keyCode;

            //tab?
            if (keyCode === 9) {
                onTab(e);
            } else if (!matches(activeElement, 'textarea') && !matches(activeElement, 'input') && !matches(activeElement, 'select') &&
                activeElement.getAttribute('contentEditable') !== "true" && activeElement.getAttribute('contentEditable') !== '' &&
                options.keyboardScrolling && options.autoScrolling) {

                //preventing the scroll with arrow keys & spacebar & Page Up & Down keys
                var keyControls = [40, 38, 32, 33, 34];
                if (keyControls.indexOf(keyCode) > -1) {
                    preventDefault(e);
                }

                controlPressed = e.ctrlKey;

                keydownId = setTimeout(function () {
                    onkeydown(e);
                }, 150);
            }
        }

        function tooltipTextHandler() {
            /*jshint validthis:true */
            trigger(prev(this), 'click');
        }

        //to prevent scrolling while zooming
        function keyUpHandler(e) {
            if (isWindowFocused) { //the keyup gets fired on new tab ctrl + t in Firefox
                controlPressed = e.ctrlKey;
            }
        }

        //binding the mousemove when the mouse's middle button is released
        function mouseDownHandler(e) {
            //middle button
            if (e.which == 2) {
                oldPageY = e.pageY;
                container.addEventListener('mousemove', mouseMoveHandler);
            }
        }

        //unbinding the mousemove when the mouse's middle button is released
        function mouseUpHandler(e) {
            //middle button
            if (e.which == 2) {
                container.removeEventListener('mousemove', mouseMoveHandler);
            }
        }

        /**
         * Makes sure the tab key will only focus elements within the current section/slide
         * preventing this way from breaking the page.
         * Based on "Modals and keyboard traps"
         * from https://developers.google.com/web/fundamentals/accessibility/focus/using-tabindex
         */
        function onTab(e) {
            var isShiftPressed = e.shiftKey;
            var activeElement = document.activeElement;
            var focusableElements = getFocusables(getSlideOrSection($(SECTION_ACTIVE_SEL)[0]));

            function preventAndFocusFirst(e) {
                preventDefault(e);
                return focusableElements[0] ? focusableElements[0].focus() : null;
            }

            //outside any section or slide? Let's not hijack the tab!
            if (isFocusOutside(e)) {
                return;
            }

            //is there an element with focus?
            if (activeElement) {
                if (closest(activeElement, SECTION_ACTIVE_SEL + ',' + SECTION_ACTIVE_SEL + ' ' + SLIDE_ACTIVE_SEL) == null) {
                    activeElement = preventAndFocusFirst(e);
                }
            }

            //no element if focused? Let's focus the first one of the section/slide
            else {
                preventAndFocusFirst(e);
            }

            //when reached the first or last focusable element of the section/slide
            //we prevent the tab action to keep it in the last focusable element
            if (!isShiftPressed && activeElement == focusableElements[focusableElements.length - 1] ||
                isShiftPressed && activeElement == focusableElements[0]
            ) {
                preventDefault(e);
            }
        }

        /**
         * Gets all the focusable elements inside the passed element.
         */
        function getFocusables(el) {
            return [].slice.call($(focusableElementsString, el)).filter(function (item) {
                return item.getAttribute('tabindex') !== '-1'
                    //are also not hidden elements (or with hidden parents)
                    && item.offsetParent !== null;
            });
        }

        /**
         * Determines whether the focus is outside fullpage.js sections/slides or not.
         */
        function isFocusOutside(e) {
            var allFocusables = getFocusables(document);
            var currentFocusIndex = allFocusables.indexOf(document.activeElement);
            var focusDestinationIndex = e.shiftKey ? currentFocusIndex - 1 : currentFocusIndex + 1;
            var focusDestination = allFocusables[focusDestinationIndex];
            var destinationItemSlide = nullOrSlide(closest(focusDestination, SLIDE_SEL));
            var destinationItemSection = nullOrSection(closest(focusDestination, SECTION_SEL));

            return !destinationItemSlide && !destinationItemSection;
        }

        //Scrolling horizontally when clicking on the slider controls.
        function slideArrowHandler() {
            /*jshint validthis:true */
            var section = closest(this, SECTION_SEL);

            /*jshint validthis:true */
            if (hasClass(this, SLIDES_PREV)) {
                if (isScrollAllowed.m.left) {
                    moveSlideLeft(section);
                }
            } else {
                if (isScrollAllowed.m.right) {
                    moveSlideRight(section);
                }
            }
        }

        //when opening a new tab (ctrl + t), `control` won't be pressed when coming back.
        function blurHandler() {
            isWindowFocused = false;
            controlPressed = false;
        }

        //Scrolls to the section when clicking the navigation bullet
        function sectionBulletHandler(e) {
            preventDefault(e);

            /*jshint validthis:true */
            var indexBullet = index(closest(this, SECTION_NAV_SEL + ' li'));
            scrollPage($(SECTION_SEL)[indexBullet]);
        }

        //Scrolls the slider to the given slide destination for the given section
        function slideBulletHandler(e) {
            preventDefault(e);

            /*jshint validthis:true */
            var slides = $(SLIDES_WRAPPER_SEL, closest(this, SECTION_SEL))[0];
            var destiny = $(SLIDE_SEL, slides)[index(closest(this, 'li'))];

            landscapeScroll(slides, destiny);
        }

        /**
         * Keydown event
         */
        function onkeydown(e) {
            var shiftPressed = e.shiftKey;

            //do nothing if we can not scroll or we are not using horizotnal key arrows.
            if (!canScroll && [37, 39].indexOf(e.keyCode) < 0) {
                return;
            }

            switch (e.keyCode) {
                //up
                case 38:
                case 33:
                    if (isScrollAllowed.k.up) {
                        moveSectionUp();
                    }
                    break;

                //down
                case 32: //spacebar
                    if (shiftPressed && isScrollAllowed.k.up) {
                        moveSectionUp();
                        break;
                    }
                /* falls through */
                case 40:
                case 34:
                    if (isScrollAllowed.k.down) {
                        moveSectionDown();
                    }
                    break;

                //Home
                case 36:
                    if (isScrollAllowed.k.up) {
                        moveTo(1);
                    }
                    break;

                //End
                case 35:
                    if (isScrollAllowed.k.down) {
                        moveTo($(SECTION_SEL).length);
                    }
                    break;

                //left
                case 37:
                    if (isScrollAllowed.k.left) {
                        moveSlideLeft();
                    }
                    break;

                //right
                case 39:
                    if (isScrollAllowed.k.right) {
                        moveSlideRight();
                    }
                    break;

                default:
                    return; // exit this handler for other keys
            }
        }

        /**
         * Detecting the direction of the mouse movement.
         * Used only for the middle button of the mouse.
         */
        var oldPageY = 0;

        function mouseMoveHandler(e) {
            if (canScroll) {
                // moving up
                if (e.pageY < oldPageY && isScrollAllowed.m.up) {
                    moveSectionUp();
                }

                // moving down
                else if (e.pageY > oldPageY && isScrollAllowed.m.down) {
                    moveSectionDown();
                }
            }
            oldPageY = e.pageY;
        }

        /**
         * Scrolls horizontal sliders.
         */
        function landscapeScroll(slides, destiny, direction) {
            var section = closest(slides, SECTION_SEL);
            var v = {
                slides: slides,
                destiny: destiny,
                direction: direction,
                destinyPos: {left: destiny.offsetLeft},
                slideIndex: index(destiny),
                section: section,
                sectionIndex: index(section, SECTION_SEL),
                anchorLink: section.getAttribute('data-anchor'),
                slidesNav: $(SLIDES_NAV_SEL, section)[0],
                slideAnchor: getAnchor(destiny),
                prevSlide: $(SLIDE_ACTIVE_SEL, section)[0],
                prevSlideIndex: index($(SLIDE_ACTIVE_SEL, section)[0]),

                //caching the value of isResizing at the momment the function is called
                //because it will be checked later inside a setTimeout and the value might change
                localIsResizing: isResizing
            };
            v.xMovement = getXmovement(v.prevSlideIndex, v.slideIndex);

            //important!! Only do it when not resizing
            if (!v.localIsResizing) {
                //preventing from scrolling to the next/prev section when using scrollHorizontally
                canScroll = false;
            }

            if (options.onSlideLeave) {

                //if the site is not just resizing and readjusting the slides
                if (!v.localIsResizing && v.xMovement !== 'none') {
                    if (isFunction(options.onSlideLeave)) {
                        if (fireCallback('onSlideLeave', v) === false) {
                            slideMoving = false;
                            return;
                        }
                    }
                }
            }

            addClass(destiny, ACTIVE);
            removeClass(siblings(destiny), ACTIVE);

            if (!v.localIsResizing) {
                stopMedia(v.prevSlide);
                lazyLoad(destiny);
            }

            if (!options.loopHorizontal && options.controlArrows) {
                //hidding it for the fist slide, showing for the rest
                toggle($(SLIDES_ARROW_PREV_SEL, section), v.slideIndex !== 0);

                //hidding it for the last slide, showing for the rest
                toggle($(SLIDES_ARROW_NEXT_SEL, section), next(destiny) != null);
            }

            //only changing the URL if the slides are in the current section (not for resize re-adjusting)
            if (hasClass(section, ACTIVE) && !v.localIsResizing) {
                setState(v.slideIndex, v.slideAnchor, v.anchorLink, v.sectionIndex);
            }

            performHorizontalMove(slides, v, true);
        }


        function afterSlideLoads(v) {
            activeSlidesNavigation(v.slidesNav, v.slideIndex);

            //if the site is not just resizing and readjusting the slides
            if (!v.localIsResizing) {
                if (isFunction(options.afterSlideLoad)) {
                    fireCallback('afterSlideLoad', v);
                }

                //needs to be inside the condition to prevent problems with continuousVertical and scrollHorizontally
                //and to prevent double scroll right after a windows resize
                canScroll = true;

                playMedia(v.destiny);
            }

            //letting them slide again
            slideMoving = false;
        }

        /**
         * Performs the horizontal movement. (CSS3 or jQuery)
         *
         * @param fireCallback {Bool} - determines whether or not to fire the callback
         */
        function performHorizontalMove(slides, v, fireCallback) {
            var destinyPos = v.destinyPos;

            if (options.css3) {
                var translate3d = 'translate3d(-' + Math.round(destinyPos.left) + 'px, 0px, 0px)';

                FP.test.translate3dH[v.sectionIndex] = translate3d;
                css(addAnimation($(SLIDES_CONTAINER_SEL, slides)), getTransforms(translate3d));

                afterSlideLoadsId = setTimeout(function () {
                    if (fireCallback) {
                        afterSlideLoads(v);
                    }
                }, options.scrollingSpeed);
            } else {
                FP.test.left[v.sectionIndex] = Math.round(destinyPos.left);

                scrollTo(slides, Math.round(destinyPos.left), options.scrollingSpeed, function () {
                    if (fireCallback) {
                        afterSlideLoads(v);
                    }
                });
            }
        }

        /**
         * Sets the state for the horizontal bullet navigations.
         */
        function activeSlidesNavigation(slidesNav, slideIndex) {
            if (options.slidesNavigation && slidesNav != null) {
                removeClass($(ACTIVE_SEL, slidesNav), ACTIVE);
                addClass($('a', $('li', slidesNav)[slideIndex]), ACTIVE);
            }
        }

        var previousHeight = windowsHeight;

        //when resizing the site, we adjust the heights of the sections, slimScroll...
        function resizeHandler() {
            //checking if it needs to get responsive
            responsive();

            // rebuild immediately on touch devices
            if (isTouchDevice) {
                var activeElement = document.activeElement;

                //if the keyboard is NOT visible
                if (!matches(activeElement, 'textarea') && !matches(activeElement, 'input') && !matches(activeElement, 'select')) {
                    var currentHeight = getWindowHeight();

                    //making sure the change in the viewport size is enough to force a rebuild. (20 % of the window to avoid problems when hidding scroll bars)
                    if (Math.abs(currentHeight - previousHeight) > (20 * Math.max(previousHeight, currentHeight) / 100)) {
                        reBuild(true);
                        previousHeight = currentHeight;
                    }
                }
            } else {
                //in order to call the functions only when the resize is finished
                //http://stackoverflow.com/questions/4298612/jquery-how-to-call-resize-event-only-once-its-finished-resizing
                clearTimeout(resizeId);

                resizeId = setTimeout(function () {
                    reBuild(true);
                }, 350);
            }
        }

        /**
         * Checks if the site needs to get responsive and disables autoScrolling if so.
         * A class `fp-responsive` is added to the plugin's container in case the user wants to use it for his own responsive CSS.
         */
        function responsive() {
            var widthLimit = options.responsive || options.responsiveWidth; //backwards compatiblity
            var heightLimit = options.responsiveHeight;

            //only calculating what we need. Remember its called on the resize event.
            var isBreakingPointWidth = widthLimit && window.innerWidth < widthLimit;
            var isBreakingPointHeight = heightLimit && window.innerHeight < heightLimit;

            if (widthLimit && heightLimit) {
                setResponsive(isBreakingPointWidth || isBreakingPointHeight);
            } else if (widthLimit) {
                setResponsive(isBreakingPointWidth);
            } else if (heightLimit) {
                setResponsive(isBreakingPointHeight);
            }
        }

        /**
         * Adds transition animations for the given element
         */
        function addAnimation(element) {
            var transition = 'all ' + options.scrollingSpeed + 'ms ' + options.easingcss3;

            removeClass(element, NO_TRANSITION);
            return css(element, {
                '-webkit-transition': transition,
                'transition': transition
            });
        }

        /**
         * Remove transition animations for the given element
         */
        function removeAnimation(element) {
            return addClass(element, NO_TRANSITION);
        }

        /**
         * Activating the vertical navigation bullets according to the given slide name.
         */
        function activateNavDots(name, sectionIndex) {
            if (options.navigation && $(SECTION_NAV_SEL)[0] != null) {
                removeClass($(ACTIVE_SEL, $(SECTION_NAV_SEL)[0]), ACTIVE);
                if (name) {
                    addClass($('a[href="#' + name + '"]', $(SECTION_NAV_SEL)[0]), ACTIVE);
                } else {
                    addClass($('a', $('li', $(SECTION_NAV_SEL)[0])[sectionIndex]), ACTIVE);
                }
            }
        }

        /**
         * Activating the website main menu elements according to the given slide name.
         */
        function activateMenuElement(name) {
            var menu = $(options.menu)[0];
            if (options.menu && menu != null) {
                removeClass($(ACTIVE_SEL, menu), ACTIVE);
                addClass($('[data-menuanchor="' + name + '"]', menu), ACTIVE);
            }
        }

        /**
         * Sets to active the current menu and vertical nav items.
         */
        function activateMenuAndNav(anchor, index) {
            activateMenuElement(anchor);
            activateNavDots(anchor, index);
        }

        /**
         * Retuns `up` or `down` depending on the scrolling movement to reach its destination
         * from the current section.
         */
        function getYmovement(destiny) {
            var fromIndex = index($(SECTION_ACTIVE_SEL)[0], SECTION_SEL);
            var toIndex = index(destiny, SECTION_SEL);
            if (fromIndex == toIndex) {
                return 'none';
            }
            if (fromIndex > toIndex) {
                return 'up';
            }
            return 'down';
        }

        /**
         * Retuns `right` or `left` depending on the scrolling movement to reach its destination
         * from the current slide.
         */
        function getXmovement(fromIndex, toIndex) {
            if (fromIndex == toIndex) {
                return 'none';
            }
            if (fromIndex > toIndex) {
                return 'left';
            }
            return 'right';
        }

        function addTableClass(element) {
            //In case we are styling for the 2nd time as in with reponsiveSlides
            if (!hasClass(element, TABLE)) {
                var wrapper = document.createElement('div');
                wrapper.className = TABLE_CELL;
                wrapper.style.height = getTableHeight(element) + 'px';

                addClass(element, TABLE);
                wrapInner(element, wrapper);
            }
        }

        function getTableHeight(element) {
            var sectionHeight = windowsHeight;

            if (options.paddingTop || options.paddingBottom) {
                var section = element;
                if (!hasClass(section, SECTION)) {
                    section = closest(element, SECTION_SEL);
                }

                var paddings = parseInt(getComputedStyle(section)['padding-top']) + parseInt(getComputedStyle(section)['padding-bottom']);
                sectionHeight = (windowsHeight - paddings);
            }

            return sectionHeight;
        }

        /**
         * Adds a css3 transform property to the container class with or without animation depending on the animated param.
         */
        function transformContainer(translate3d, animated) {
            if (animated) {
                addAnimation(container);
            } else {
                removeAnimation(container);
            }

            css(container, getTransforms(translate3d));
            FP.test.translate3d = translate3d;

            //syncronously removing the class after the animation has been applied.
            setTimeout(function () {
                removeClass(container, NO_TRANSITION);
            }, 10);
        }

        /**
         * Gets a section by its anchor / index
         */
        function getSectionByAnchor(sectionAnchor) {
            var section = $(SECTION_SEL + '[data-anchor="' + sectionAnchor + '"]', container)[0];
            if (!section) {
                var sectionIndex = typeof sectionAnchor !== 'undefined' ? sectionAnchor - 1 : 0;
                section = $(SECTION_SEL)[sectionIndex];
            }

            return section;
        }

        /**
         * Gets a slide inside a given section by its anchor / index
         */
        function getSlideByAnchor(slideAnchor, section) {
            var slide = $(SLIDE_SEL + '[data-anchor="' + slideAnchor + '"]', section)[0];
            if (slide == null) {
                slideAnchor = typeof slideAnchor !== 'undefined' ? slideAnchor : 0;
                slide = $(SLIDE_SEL, section)[slideAnchor];
            }

            return slide;
        }

        /**
         * Scrolls to the given section and slide anchors
         */
        function scrollPageAndSlide(sectionAnchor, slideAnchor) {
            var section = getSectionByAnchor(sectionAnchor);

            //do nothing if there's no section with the given anchor name
            if (section == null) return;

            var slide = getSlideByAnchor(slideAnchor, section);

            //we need to scroll to the section and then to the slide
            if (getAnchor(section) !== lastScrolledDestiny && !hasClass(section, ACTIVE)) {
                scrollPage(section, function () {
                    scrollSlider(slide);
                });
            }
            //if we were already in the section
            else {
                scrollSlider(slide);
            }
        }

        /**
         * Scrolls the slider to the given slide destination for the given section
         */
        function scrollSlider(slide) {
            if (slide != null) {
                landscapeScroll(closest(slide, SLIDES_WRAPPER_SEL), slide);
            }
        }

        /**
         * Creates a landscape navigation bar with dots for horizontal sliders.
         */
        function addSlidesNavigation(section, numSlides) {
            appendTo(createElementFromHTML('<div class="' + SLIDES_NAV + '"><ul></ul></div>'), section);
            var nav = $(SLIDES_NAV_SEL, section)[0];

            //top or bottom
            addClass(nav, 'fp-' + options.slidesNavPosition);

            for (var i = 0; i < numSlides; i++) {
                appendTo(createElementFromHTML('<li><a href="#"><span class="fp-sr-only">' + getBulletLinkName(i, 'Slide') + '</span><span></span></a></li>'), $('ul', nav)[0]);
            }

            //centering it
            css(nav, {'margin-left': '-' + (nav.innerWidth / 2) + 'px'});

            addClass($('a', $('li', nav)[0]), ACTIVE);
        }


        /**
         * Sets the state of the website depending on the active section/slide.
         * It changes the URL hash when needed and updates the body class.
         */
        function setState(slideIndex, slideAnchor, anchorLink, sectionIndex) {
            var sectionHash = '';

            if (options.anchors.length && !options.lockAnchors) {

                //isn't it the first slide?
                if (slideIndex) {
                    if (anchorLink != null) {
                        sectionHash = anchorLink;
                    }

                    //slide without anchor link? We take the index instead.
                    if (slideAnchor == null) {
                        slideAnchor = slideIndex;
                    }

                    lastScrolledSlide = slideAnchor;
                    setUrlHash(sectionHash + '/' + slideAnchor);

                    //first slide won't have slide anchor, just the section one
                } else if (slideIndex != null) {
                    lastScrolledSlide = slideAnchor;
                    setUrlHash(anchorLink);
                }

                //section without slides
                else {
                    setUrlHash(anchorLink);
                }
            }

            setBodyClass();
        }

        /**
         * Sets the URL hash.
         */
        function setUrlHash(url) {
            if (options.recordHistory) {
                location.hash = url;
            } else {
                //Mobile Chrome doesn't work the normal way, so... lets use HTML5 for phones :)
                if (isTouchDevice || isTouch) {
                    window.history.replaceState(undefined, undefined, '#' + url);
                } else {
                    var baseUrl = window.location.href.split('#')[0];
                    window.location.replace(baseUrl + '#' + url);
                }
            }
        }

        /**
         * Gets the anchor for the given slide / section. Its index will be used if there's none.
         */
        function getAnchor(element) {
            if (!element) {
                return null;
            }
            var anchor = element.getAttribute('data-anchor');
            var elementIndex = index(element);

            //Slide without anchor link? We take the index instead.
            if (anchor == null) {
                anchor = elementIndex;
            }

            return anchor;
        }

        /**
         * Sets a class for the body of the page depending on the active section / slide
         */
        function setBodyClass() {
            var section = $(SECTION_ACTIVE_SEL)[0];
            var slide = $(SLIDE_ACTIVE_SEL, section)[0];

            var sectionAnchor = getAnchor(section);
            var slideAnchor = getAnchor(slide);

            var text = String(sectionAnchor);

            if (slide) {
                text = text + '-' + slideAnchor;
            }

            //changing slash for dash to make it a valid CSS style
            text = text.replace('/', '-').replace('#', '');

            //removing previous anchor classes
            var classRe = new RegExp('\\b\\s?' + VIEWING_PREFIX + '-[^\\s]+\\b', "g");
            $body.className = $body.className.replace(classRe, '');

            //adding the current anchor
            addClass($body, VIEWING_PREFIX + '-' + text);
        }

        /**
         * Checks for translate3d support
         * @return boolean
         * http://stackoverflow.com/questions/5661671/detecting-transform-translate3d-support
         */
        function support3d() {
            var el = document.createElement('p'),
                has3d,
                transforms = {
                    'webkitTransform': '-webkit-transform',
                    'OTransform': '-o-transform',
                    'msTransform': '-ms-transform',
                    'MozTransform': '-moz-transform',
                    'transform': 'transform'
                };

            //preventing the style p:empty{display: none;} from returning the wrong result
            el.style.display = 'block'

            // Add it to the body to get the computed style.
            document.body.insertBefore(el, null);

            for (var t in transforms) {
                if (el.style[t] !== undefined) {
                    el.style[t] = 'translate3d(1px,1px,1px)';
                    has3d = window.getComputedStyle(el).getPropertyValue(transforms[t]);
                }
            }

            document.body.removeChild(el);

            return (has3d !== undefined && has3d.length > 0 && has3d !== 'none');
        }

        /**
         * Removes the auto scrolling action fired by the mouse wheel and trackpad.
         * After this function is called, the mousewheel and trackpad movements won't scroll through sections.
         */
        function removeMouseWheelHandler() {
            if (document.addEventListener) {
                document.removeEventListener('mousewheel', MouseWheelHandler, false); //IE9, Chrome, Safari, Oper
                document.removeEventListener('wheel', MouseWheelHandler, false); //Firefox
                document.removeEventListener('MozMousePixelScroll', MouseWheelHandler, false); //old Firefox
            } else {
                document.detachEvent('onmousewheel', MouseWheelHandler); //IE 6/7/8
            }
        }

        /**
         * Adds the auto scrolling action for the mouse wheel and trackpad.
         * After this function is called, the mousewheel and trackpad movements will scroll through sections
         * https://developer.mozilla.org/en-US/docs/Web/Events/wheel
         */
        function addMouseWheelHandler() {
            var prefix = '';
            var _addEventListener;

            if (window.addEventListener) {
                _addEventListener = "addEventListener";
            } else {
                _addEventListener = "attachEvent";
                prefix = 'on';
            }

            // detect available wheel event
            var support = 'onwheel' in document.createElement('div') ? 'wheel' : // Modern browsers support "wheel"
                document.onmousewheel !== undefined ? 'mousewheel' : // Webkit and IE support at least "mousewheel"
                    'DOMMouseScroll'; // let's assume that remaining browsers are older Firefox


            if (support == 'DOMMouseScroll') {
                document[_addEventListener](prefix + 'MozMousePixelScroll', MouseWheelHandler, false);
            }

            //handle MozMousePixelScroll in older Firefox
            else {
                document[_addEventListener](prefix + support, MouseWheelHandler, false);
            }
        }

        /**
         * Binding the mousemove when the mouse's middle button is pressed
         */
        function addMiddleWheelHandler() {
            container.addEventListener('mousedown', mouseDownHandler);
            container.addEventListener('mouseup', mouseUpHandler);
        }

        /**
         * Unbinding the mousemove when the mouse's middle button is released
         */
        function removeMiddleWheelHandler() {
            container.removeEventListener('mousedown', mouseDownHandler);
            container.removeEventListener('mouseup', mouseUpHandler);
        }

        /**
         * Adds the possibility to auto scroll through sections on touch devices.
         */
        function addTouchHandler() {
            if (isTouchDevice || isTouch) {
                if (options.autoScrolling) {
                    $body.removeEventListener(events.touchmove, preventBouncing, {passive: false});
                    $body.addEventListener(events.touchmove, preventBouncing, {passive: false});
                }

                $(WRAPPER_SEL)[0].removeEventListener(events.touchstart, touchStartHandler);
                $(WRAPPER_SEL)[0].removeEventListener(events.touchmove, touchMoveHandler, {passive: false});

                $(WRAPPER_SEL)[0].addEventListener(events.touchstart, touchStartHandler);
                $(WRAPPER_SEL)[0].addEventListener(events.touchmove, touchMoveHandler, {passive: false});
            }
        }

        /**
         * Removes the auto scrolling for touch devices.
         */
        function removeTouchHandler() {
            if (isTouchDevice || isTouch) {
                // normalScrollElements requires it off #2691
                if (options.autoScrolling) {
                    $body.removeEventListener(events.touchmove, touchMoveHandler, {passive: false});
                    $body.removeEventListener(events.touchmove, preventBouncing, {passive: false});
                }

                $(WRAPPER_SEL)[0].removeEventListener(events.touchstart, touchStartHandler);
                $(WRAPPER_SEL)[0].removeEventListener(events.touchmove, touchMoveHandler, {passive: false});
            }
        }

        /*
        * Returns and object with Microsoft pointers (for IE<11 and for IE >= 11)
        * http://msdn.microsoft.com/en-us/library/ie/dn304886(v=vs.85).aspx
        */
        function getMSPointer() {
            var pointer;

            //IE >= 11 & rest of browsers
            if (window.PointerEvent) {
                pointer = {down: 'pointerdown', move: 'pointermove'};
            }

            //IE < 11
            else {
                pointer = {down: 'MSPointerDown', move: 'MSPointerMove'};
            }

            return pointer;
        }

        /**
         * Gets the pageX and pageY properties depending on the browser.
         * https://github.com/alvarotrigo/fullPage.js/issues/194#issuecomment-34069854
         */
        function getEventsPage(e) {
            var events = [];

            events.y = (typeof e.pageY !== 'undefined' && (e.pageY || e.pageX) ? e.pageY : e.touches[0].pageY);
            events.x = (typeof e.pageX !== 'undefined' && (e.pageY || e.pageX) ? e.pageX : e.touches[0].pageX);

            //in touch devices with scrollBar:true, e.pageY is detected, but we have to deal with touch events. #1008
            if (isTouch && isReallyTouch(e) && options.scrollBar && typeof e.touches !== 'undefined') {
                events.y = e.touches[0].pageY;
                events.x = e.touches[0].pageX;
            }

            return events;
        }

        /**
         * Slides silently (with no animation) the active slider to the given slide.
         * @param noCallback {bool} true or defined -> no callbacks
         */
        function silentLandscapeScroll(activeSlide, noCallbacks) {
            setScrollingSpeed(0, 'internal');

            if (typeof noCallbacks !== 'undefined') {
                //preventing firing callbacks afterSlideLoad etc.
                isResizing = true;
            }

            landscapeScroll(closest(activeSlide, SLIDES_WRAPPER_SEL), activeSlide);

            if (typeof noCallbacks !== 'undefined') {
                isResizing = false;
            }

            setScrollingSpeed(originals.scrollingSpeed, 'internal');
        }

        /**
         * Scrolls silently (with no animation) the page to the given Y position.
         */
        function silentScroll(top) {
            // The first section can have a negative value in iOS 10. Not quite sure why: -0.0142822265625
            // that's why we round it to 0.
            var roundedTop = Math.round(top);

            if (options.css3 && options.autoScrolling && !options.scrollBar) {
                var translate3d = 'translate3d(0px, -' + roundedTop + 'px, 0px)';
                transformContainer(translate3d, false);
            } else if (options.autoScrolling && !options.scrollBar) {
                css(container, {'top': -roundedTop + 'px'});
                FP.test.top = -roundedTop + 'px';
            } else {
                var scrollSettings = getScrollSettings(roundedTop);
                setScrolling(scrollSettings.element, scrollSettings.options);
            }
        }

        /**
         * Returns the cross-browser transform string.
         */
        function getTransforms(translate3d) {
            return {
                '-webkit-transform': translate3d,
                '-moz-transform': translate3d,
                '-ms-transform': translate3d,
                'transform': translate3d
            };
        }

        /**
         * Allowing or disallowing the mouse/swipe scroll in a given direction. (not for keyboard)
         * @type  m (mouse) or k (keyboard)
         */
        function setIsScrollAllowed(value, direction, type) {
            //up, down, left, right
            if (direction !== 'all') {
                isScrollAllowed[type][direction] = value;
            }

            //all directions?
            else {
                Object.keys(isScrollAllowed[type]).forEach(function (key) {
                    isScrollAllowed[type][key] = value;
                });
            }
        }

        /*
        * Destroys fullpage.js plugin events and optinally its html markup and styles
        */
        function destroy(all) {
            setAutoScrolling(false, 'internal');
            setAllowScrolling(true);
            setMouseHijack(false);
            setKeyboardScrolling(false);
            addClass(container, DESTROYED);

            clearTimeout(afterSlideLoadsId);
            clearTimeout(afterSectionLoadsId);
            clearTimeout(resizeId);
            clearTimeout(scrollId);
            clearTimeout(scrollId2);


            window.removeEventListener('scroll', scrollHandler);
            window.removeEventListener('hashchange', hashChangeHandler);
            window.removeEventListener('resize', resizeHandler);

            document.removeEventListener('keydown', keydownHandler);
            document.removeEventListener('keyup', keyUpHandler);

            ['click', 'touchstart'].forEach(function (eventName) {
                document.removeEventListener(eventName, delegatedEvents);
            });

            ['mouseenter', 'touchstart', 'mouseleave', 'touchend'].forEach(function (eventName) {
                document.removeEventListener(eventName, onMouseEnterOrLeave, true); //true is required!
            });

            clearTimeout(afterSlideLoadsId);
            clearTimeout(afterSectionLoadsId);

            //lets make a mess!
            if (all) {
                destroyStructure();
            }
        }

        /*
        * Removes inline styles added by fullpage.js
        */
        function destroyStructure() {
            //reseting the `top` or `translate` properties to 0
            silentScroll(0);

            //loading all the lazy load content
            $('img[data-src], source[data-src], audio[data-src], iframe[data-src]', container).forEach(function (item) {
                setSrc(item, 'src');
            });

            $('img[data-srcset]').forEach(function (item) {
                setSrc(item, 'srcset');
            });

            remove($(SECTION_NAV_SEL + ', ' + SLIDES_NAV_SEL + ', ' + SLIDES_ARROW_SEL));

            //removing inline styles
            css($(SECTION_SEL), {
                'height': '',
                'background-color': '',
                'padding': ''
            });

            css($(SLIDE_SEL), {
                'width': ''
            });

            css(container, {
                'height': '',
                'position': '',
                '-ms-touch-action': '',
                'touch-action': ''
            });

            css($htmlBody, {
                'overflow': '',
                'height': ''
            });

            // remove .fp-enabled class
            removeClass($('html'), ENABLED);

            // remove .fp-responsive class
            removeClass($body, RESPONSIVE);

            // remove all of the .fp-viewing- classes
            $body.className.split(/\s+/).forEach(function (className) {
                if (className.indexOf(VIEWING_PREFIX) === 0) {
                    removeClass($body, className);
                }
            });

            //removing added classes
            $(SECTION_SEL + ', ' + SLIDE_SEL).forEach(function (item) {
                if (options.scrollOverflowHandler) {
                    options.scrollOverflowHandler.remove(item);
                }
                removeClass(item, TABLE + ' ' + ACTIVE + ' ' + COMPLETELY);
                var previousStyles = item.getAttribute('data-fp-styles');
                if (previousStyles) {
                    item.setAttribute('style', item.getAttribute('data-fp-styles'));
                }
            });

            //removing the applied transition from the fullpage wrapper
            removeAnimation(container);

            //Unwrapping content
            [TABLE_CELL_SEL, SLIDES_CONTAINER_SEL, SLIDES_WRAPPER_SEL].forEach(function (selector) {
                $(selector, container).forEach(function (item) {
                    //unwrap not being use in case there's no child element inside and its just text
                    item.outerHTML = item.innerHTML;
                });
            });

            //removing the applied transition from the fullpage wrapper
            css(container, {
                '-webkit-transition': 'none',
                'transition': 'none'
            });

            //scrolling the page to the top with no animation
            window.scrollTo(0, 0);

            //removing selectors
            var usedSelectors = [SECTION, SLIDE, SLIDES_CONTAINER];
            usedSelectors.forEach(function (item) {
                removeClass($('.' + item), item);
            });
        }

        /*
        * Sets the state for a variable with multiple states (original, and temporal)
        * Some variables such as `autoScrolling` or `recordHistory` might change automatically its state when using `responsive` or `autoScrolling:false`.
        * This function is used to keep track of both states, the original and the temporal one.
        * If type is not 'internal', then we assume the user is globally changing the variable.
        */
        function setVariableState(variable, value, type) {
            options[variable] = value;
            if (type !== 'internal') {
                originals[variable] = value;
            }
        }

        /**
         * Displays warnings
         */
        function displayWarnings() {
            if (!isLicenseValid) {
                showError('error', 'Fullpage.js version 3 has changed its license to GPLv3 and it requires a `licenseKey` option. Read about it here:');
                showError('error', 'https://github.com/alvarotrigo/fullPage.js#options.');
            }

            var extensions = ['fadingEffect', 'continuousHorizontal', 'scrollHorizontally', 'interlockedSlides', 'resetSliders', 'responsiveSlides', 'offsetSections', 'dragAndMove', 'scrollOverflowReset', 'parallax'];
            if (hasClass($('html'), ENABLED)) {
                showError('error', 'Fullpage.js can only be initialized once and you are doing it multiple times!');
                return;
            }

            // Disable mutually exclusive settings
            if (options.continuousVertical &&
                (options.loopTop || options.loopBottom)) {
                options.continuousVertical = false;
                showError('warn', 'Option `loopTop/loopBottom` is mutually exclusive with `continuousVertical`; `continuousVertical` disabled');
            }

            if (options.scrollOverflow &&
                (options.scrollBar || !options.autoScrolling)) {
                showError('warn', 'Options scrollBar:true and autoScrolling:false are mutually exclusive with scrollOverflow:true. Sections with scrollOverflow might not work well in Firefox');
            }

            if (options.continuousVertical && (options.scrollBar || !options.autoScrolling)) {
                options.continuousVertical = false;
                showError('warn', 'Scroll bars (`scrollBar:true` or `autoScrolling:false`) are mutually exclusive with `continuousVertical`; `continuousVertical` disabled');
            }

            if (options.scrollOverflow && options.scrollOverflowHandler == null) {
                options.scrollOverflow = false;
                showError('error', 'The option `scrollOverflow:true` requires the file `scrolloverflow.min.js`. Please include it before fullPage.js.');
            }

            //using extensions? Wrong file!
            extensions.forEach(function (extension) {
                //is the option set to true?
                if (options[extension]) {
                    showError('warn', 'fullpage.js extensions require fullpage.extensions.min.js file instead of the usual fullpage.js. Requested: ' + extension);
                }
            });

            //anchors can not have the same value as any element ID or NAME
            options.anchors.forEach(function (name) {

                //case insensitive selectors (http://stackoverflow.com/a/19465187/1081396)
                var nameAttr = [].slice.call($('[name]')).filter(function (item) {
                    return item.getAttribute('name') && item.getAttribute('name').toLowerCase() == name.toLowerCase();
                });

                var idAttr = [].slice.call($('[id]')).filter(function (item) {
                    return item.getAttribute('id') && item.getAttribute('id').toLowerCase() == name.toLowerCase();
                });

                if (idAttr.length || nameAttr.length) {
                    showError('error', 'data-anchor tags can not have the same value as any `id` element on the site (or `name` element for IE).');
                    if (idAttr.length) {
                        showError('error', '"' + name + '" is is being used by another element `id` property');
                    }
                    if (nameAttr.length) {
                        showError('error', '"' + name + '" is is being used by another element `name` property');
                    }
                }
            });
        }

        /**
         * Getting the position of the element to scroll when using jQuery animations
         */
        function getScrolledPosition(element) {
            var position;

            //is not the window element and is a slide?
            if (element.self != window && hasClass(element, SLIDES_WRAPPER)) {
                position = element.scrollLeft;
            } else if (!options.autoScrolling || options.scrollBar) {
                position = getScrollTop();
            } else {
                position = element.offsetTop;
            }

            //gets the top property of the wrapper
            return position;
        }

        /**
         * Simulates the animated scrollTop of jQuery. Used when css3:false or scrollBar:true or autoScrolling:false
         * http://stackoverflow.com/a/16136789/1081396
         */
        function scrollTo(element, to, duration, callback) {
            var start = getScrolledPosition(element);
            var change = to - start;
            var currentTime = 0;
            var increment = 20;
            activeAnimation = true;

            var animateScroll = function () {
                if (activeAnimation) { //in order to stope it from other function whenever we want
                    var val = to;

                    currentTime += increment;

                    if (duration) {
                        val = window.fp_easings[options.easing](currentTime, start, change, duration);
                    }

                    setScrolling(element, val);

                    if (currentTime < duration) {
                        setTimeout(animateScroll, increment);
                    } else if (typeof callback !== 'undefined') {
                        callback();
                    }
                } else if (currentTime < duration) {
                    callback();
                }
            };

            animateScroll();
        }

        /**
         * Scrolls the page / slider the given number of pixels.
         * It will do it one or another way dependiong on the library's config.
         */
        function setScrolling(element, val) {
            if (!options.autoScrolling || options.scrollBar || (element.self != window && hasClass(element, SLIDES_WRAPPER))) {

                //scrolling horizontally through the slides?
                if (element.self != window && hasClass(element, SLIDES_WRAPPER)) {
                    element.scrollLeft = val;
                }
                //vertical scroll
                else {
                    element.scrollTo(0, val);
                }
            } else {
                element.style.top = val + 'px';
            }
        }

        /**
         * Gets the active slide.
         */
        function getActiveSlide() {
            var activeSlide = $(SLIDE_ACTIVE_SEL, $(SECTION_ACTIVE_SEL)[0])[0];
            return nullOrSlide(activeSlide);
        }

        /**
         * Gets the active section.
         */
        function getActiveSection() {
            return new Section($(SECTION_ACTIVE_SEL)[0]);
        }

        /**
         * Item. Slide or Section objects share the same properties.
         */
        function Item(el, selector) {
            this.anchor = el.getAttribute('data-anchor');
            this.item = el;
            this.index = index(el, selector);
            this.isLast = this.index === $(selector).length - 1;
            this.isFirst = !this.index;
        }

        /**
         * Section object
         */
        function Section(el) {
            Item.call(this, el, SECTION_SEL);
        }

        /**
         * Slide object
         */
        function Slide(el) {
            Item.call(this, el, SLIDE_SEL);
        }

        return FP;
    } //end of $.fn.fullpage


    //utils
    /**
     * Shows a message in the console of the given type.
     */
    function showError(type, text) {
        window.console && window.console[type] && window.console[type]('fullPage: ' + text);
    }

    /**
     * Equivalent or jQuery function $().
     */
    function $(selector, context) {
        context = arguments.length > 1 ? context : document;
        return context ? context.querySelectorAll(selector) : null;
    }

    /**
     * Extends a given Object properties and its childs.
     */
    function deepExtend(out) {
        out = out || {};

        for (var i = 1; i < arguments.length; i++) {
            var obj = arguments[i];

            if (!obj)
                continue;

            for (var key in obj) {
                if (obj.hasOwnProperty(key)) {
                    if (typeof obj[key] === 'object' && obj[key] != null)
                        out[key] = deepExtend(out[key], obj[key]);
                    else
                        out[key] = obj[key];
                }
            }
        }

        return out;
    }

    /**
     * Checks if the passed element contains the passed class.
     */
    function hasClass(el, className) {
        if (el == null) {
            return false;
        }
        if (el.classList) {
            return el.classList.contains(className);
        }
        return new RegExp('(^| )' + className + '( |$)', 'gi').test(el.className);
    }

    /**
     * Gets the window height. Crossbrowser.
     */
    function getWindowHeight() {
        return 'innerHeight' in window ? window.innerHeight : document.documentElement.offsetHeight;
    }

    /**
     * Set's the CSS properties for the passed item/s.
     * @param {NodeList|HTMLElement} items
     * @param {Object} props css properties and values.
     */
    function css(items, props) {
        items = getList(items);

        var key;
        for (key in props) {
            if (props.hasOwnProperty(key)) {
                if (key !== null) {
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        item.style[key] = props[key];
                    }
                }
            }
        }

        return items;
    }

    /**
     * Generic function to get the previous or next element.
     */
    function until(item, selector, fn) {
        var sibling = item[fn];
        while (sibling && !matches(sibling, selector)) {
            sibling = sibling[fn];
        }

        return sibling;
    }

    /**
     * Gets the previous element to the passed element that matches the passed selector.
     */
    function prevUntil(item, selector) {
        return until(item, selector, 'previousElementSibling');
    }

    /**
     * Gets the next element to the passed element that matches the passed selector.
     */
    function nextUntil(item, selector) {
        return until(item, selector, 'nextElementSibling');
    }

    /**
     * Gets the previous element to the passed element.
     */
    function prev(item) {
        return item.previousElementSibling;
    }

    /**
     * Gets the next element to the passed element.
     */
    function next(item) {
        return item.nextElementSibling;
    }

    /**
     * Gets the last element from the passed list of elements.
     */
    function last(item) {
        return item[item.length - 1];
    }

    /**
     * Gets index from the passed element.
     * @param {String} selector is optional.
     */
    function index(item, selector) {
        item = isArrayOrList(item) ? item[0] : item;
        var children = selector != null ? $(selector, item.parentNode) : item.parentNode.childNodes;
        var num = 0;
        for (var i = 0; i < children.length; i++) {
            if (children[i] == item) return num;
            if (children[i].nodeType == 1) num++;
        }
        return -1;
    }

    /**
     * Gets an iterable element for the passed element/s
     */
    function getList(item) {
        return !isArrayOrList(item) ? [item] : item;
    }

    /**
     * Adds the display=none property for the passed element/s
     */
    function hide(el) {
        el = getList(el);

        for (var i = 0; i < el.length; i++) {
            el[i].style.display = 'none';
        }
        return el;
    }

    /**
     * Adds the display=block property for the passed element/s
     */
    function show(el) {
        el = getList(el);

        for (var i = 0; i < el.length; i++) {
            el[i].style.display = 'block';
        }
        return el;
    }

    /**
     * Checks if the passed element is an iterable element or not
     */
    function isArrayOrList(el) {
        return Object.prototype.toString.call(el) === '[object Array]' ||
            Object.prototype.toString.call(el) === '[object NodeList]';
    }

    /**
     * Adds the passed class to the passed element/s
     */
    function addClass(el, className) {
        el = getList(el);

        for (var i = 0; i < el.length; i++) {
            var item = el[i];
            if (item.classList) {
                item.classList.add(className);
            } else {
                item.className += ' ' + className;
            }
        }
        return el;
    }

    /**
     * Removes the passed class to the passed element/s
     * @param {String} `className` can be multiple classnames separated by whitespace
     */
    function removeClass(el, className) {
        el = getList(el);

        var classNames = className.split(' ');

        for (var a = 0; a < classNames.length; a++) {
            className = classNames[a];
            for (var i = 0; i < el.length; i++) {
                var item = el[i];
                if (item.classList) {
                    item.classList.remove(className);
                } else {
                    item.className = item.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
                }
            }
        }
        return el;
    }

    /**
     * Appends the given element ot the given parent.
     */
    function appendTo(el, parent) {
        parent.appendChild(el);
    }

    /**
     Usage:

     var wrapper = document.createElement('div');
     wrapper.className = 'fp-slides';
     wrap($('.slide'), wrapper);

     https://jsfiddle.net/qwzc7oy3/15/ (vanilla)
     https://jsfiddle.net/oya6ndka/1/ (jquery equivalent)
     */
    function wrap(toWrap, wrapper, isWrapAll) {
        var newParent;
        wrapper = wrapper || document.createElement('div');
        for (var i = 0; i < toWrap.length; i++) {
            var item = toWrap[i];
            if (isWrapAll && !i || !isWrapAll) {
                newParent = wrapper.cloneNode(true);
                item.parentNode.insertBefore(newParent, item);
            }
            newParent.appendChild(item);
        }
        return toWrap;
    }

    /**
     Usage:
     var wrapper = document.createElement('div');
     wrapper.className = 'fp-slides';
     wrap($('.slide'), wrapper);

     https://jsfiddle.net/qwzc7oy3/27/ (vanilla)
     https://jsfiddle.net/oya6ndka/4/ (jquery equivalent)
     */
    function wrapAll(toWrap, wrapper) {
        wrap(toWrap, wrapper, true);
    }

    /**
     * Usage:
     * wrapInner(document.querySelector('#pepe'), '<div class="test">afdas</div>');
     * wrapInner(document.querySelector('#pepe'), element);
     *
     * https://jsfiddle.net/zexxz0tw/6/
     *
     * https://stackoverflow.com/a/21817590/1081396
     */
    function wrapInner(parent, wrapper) {
        if (typeof wrapper === "string") {
            wrapper = createElementFromHTML(wrapper);
        }

        parent.appendChild(wrapper);

        while (parent.firstChild !== wrapper) {
            wrapper.appendChild(parent.firstChild);
        }
    }

    /**
     * http://stackoverflow.com/questions/22100853/dom-pure-javascript-solution-to-jquery-closest-implementation
     * Returns the element or `false` if there's none
     */
    function closest(el, selector) {
        if (el && el.nodeType === 1) {
            if (matches(el, selector)) {
                return el;
            }
            return closest(el.parentNode, selector);
        }
        return null;
    }

    /**
     * Places one element (rel) after another one or group of them (reference).
     * @param {HTMLElement} reference
     * @param {HTMLElement|NodeList|String} el
     * https://jsfiddle.net/9s97hhzv/1/
     */
    function after(reference, el) {
        insertBefore(reference, reference.nextSibling, el);
    }

    /**
     * Places one element (rel) before another one or group of them (reference).
     * @param {HTMLElement} reference
     * @param {HTMLElement|NodeList|String} el
     * https://jsfiddle.net/9s97hhzv/1/
     */
    function before(reference, el) {
        insertBefore(reference, reference, el);
    }

    /**
     * Based in https://stackoverflow.com/a/19316024/1081396
     * and https://stackoverflow.com/a/4793630/1081396
     */
    function insertBefore(reference, beforeElement, el) {
        if (!isArrayOrList(el)) {
            if (typeof el == 'string') {
                el = createElementFromHTML(el);
            }
            el = [el];
        }

        for (var i = 0; i < el.length; i++) {
            reference.parentNode.insertBefore(el[i], beforeElement);
        }
    }

    //http://stackoverflow.com/questions/3464876/javascript-get-window-x-y-position-for-scroll
    function getScrollTop() {
        var doc = document.documentElement;
        return (window.pageYOffset || doc.scrollTop) - (doc.clientTop || 0);
    }

    /**
     * Gets the siblings of the passed element
     */
    function siblings(el) {
        return Array.prototype.filter.call(el.parentNode.children, function (child) {
            return child !== el;
        });
    }

    //for IE 9 ?
    function preventDefault(event) {
        if (event.preventDefault) {
            event.preventDefault();
        } else {
            event.returnValue = false;
        }
    }

    /**
     * Determines whether the passed item is of function type.
     */
    function isFunction(item) {
        if (typeof item === 'function') {
            return true;
        }
        var type = Object.prototype.toString(item);
        return type === '[object Function]' || type === '[object GeneratorFunction]';
    }

    /**
     * Trigger custom events
     */
    function trigger(el, eventName, data) {
        var event;
        data = typeof data === 'undefined' ? {} : data;

        // Native
        if (typeof window.CustomEvent === "function") {
            event = new CustomEvent(eventName, {detail: data});
        } else {
            event = document.createEvent('CustomEvent');
            event.initCustomEvent(eventName, true, true, data);
        }

        el.dispatchEvent(event);
    }

    /**
     * Polyfill of .matches()
     */
    function matches(el, selector) {
        return (el.matches || el.matchesSelector || el.msMatchesSelector || el.mozMatchesSelector || el.webkitMatchesSelector || el.oMatchesSelector).call(el, selector);
    }

    /**
     * Toggles the visibility of the passed element el.
     */
    function toggle(el, value) {
        if (typeof value === "boolean") {
            for (var i = 0; i < el.length; i++) {
                el[i].style.display = value ? 'block' : 'none';
            }
        }
        //we don't use it in other way, so no else :)

        return el;
    }

    /**
     * Creates a HTMLElement from the passed HTML string.
     * https://stackoverflow.com/a/494348/1081396
     */
    function createElementFromHTML(htmlString) {
        var div = document.createElement('div');
        div.innerHTML = htmlString.trim();

        // Change this to div.childNodes to support multiple top-level nodes
        return div.firstChild;
    }

    /**
     * Removes the passed item/s from the DOM.
     */
    function remove(items) {
        items = getList(items);
        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            if (item && item.parentElement) {
                item.parentNode.removeChild(item);
            }
        }
    }

    /**
     * Filters an array by the passed filter funtion.
     */
    function filter(el, filterFn) {
        Array.prototype.filter.call(el, filterFn);
    }

    //https://jsfiddle.net/w1rktecz/
    function untilAll(item, selector, fn) {
        var sibling = item[fn];
        var siblings = [];
        while (sibling) {
            if (matches(sibling, selector) || selector == null) {
                siblings.push(sibling);
            }
            sibling = sibling[fn];
        }

        return siblings;
    }

    /**
     * Gets all next elements matching the passed selector.
     */
    function nextAll(item, selector) {
        return untilAll(item, selector, 'nextElementSibling');
    }

    /**
     * Gets all previous elements matching the passed selector.
     */
    function prevAll(item, selector) {
        return untilAll(item, selector, 'previousElementSibling');
    }

    /**
     * Converts an object to an array.
     */
    function toArray(objectData) {
        return Object.keys(objectData).map(function (key) {
            return objectData[key];
        });
    }

    /**
     * forEach polyfill for IE
     * https://developer.mozilla.org/en-US/docs/Web/API/NodeList/forEach#Browser_Compatibility
     */
    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = function (callback, thisArg) {
            thisArg = thisArg || window;
            for (var i = 0; i < this.length; i++) {
                callback.call(thisArg, this[i], i, this);
            }
        };
    }

    //utils are public, so we can use it wherever we want
    window.fp_utils = {
        $: $,
        deepExtend: deepExtend,
        hasClass: hasClass,
        getWindowHeight: getWindowHeight,
        css: css,
        until: until,
        prevUntil: prevUntil,
        nextUntil: nextUntil,
        prev: prev,
        next: next,
        last: last,
        index: index,
        getList: getList,
        hide: hide,
        show: show,
        isArrayOrList: isArrayOrList,
        addClass: addClass,
        removeClass: removeClass,
        appendTo: appendTo,
        wrap: wrap,
        wrapAll: wrapAll,
        wrapInner: wrapInner,
        closest: closest,
        after: after,
        before: before,
        insertBefore: insertBefore,
        getScrollTop: getScrollTop,
        siblings: siblings,
        preventDefault: preventDefault,
        isFunction: isFunction,
        trigger: trigger,
        matches: matches,
        toggle: toggle,
        createElementFromHTML: createElementFromHTML,
        remove: remove,
        filter: filter,
        untilAll: untilAll,
        nextAll: nextAll,
        prevAll: prevAll,
        showError: showError
    };

    return initialise;
}));

/**
 * jQuery adapter for fullPage.js 3.0.0
 */
if (window.jQuery && window.fullpage) {
    (function ($, fullpage) {
        'use strict';

        // No jQuery No Go
        if (!$ || !fullpage) {
            window.fp_utils.showError('error', 'jQuery is required to use the jQuery fullpage adapter!');
            return;
        }

        $.fn.fullpage = function (options) {
            var FP = new fullpage('#' + $(this).attr('id'), options);

            //Static API
            Object.keys(FP).forEach(function (key) {
                $.fn.fullpage[key] = FP[key];
            });
        };
    })(window.jQuery, window.fullpage);
}

/**
 * Swiper 4.4.1
 * Most modern mobile touch slider and framework with hardware accelerated transitions
 * http://www.idangero.us/swiper/
 *
 * Copyright 2014-2018 Vladimir Kharlampidi
 *
 * Released under the MIT License
 *
 * Released on: September 14, 2018
 */
!function (e, t) {
    "object" == typeof exports && "undefined" != typeof module ? module.exports = t() : "function" == typeof define && define.amd ? define(t) : e.Swiper = t()
}(this, function () {
    "use strict";
    var f = "undefined" == typeof document ? {
        body: {}, addEventListener: function () {
        }, removeEventListener: function () {
        }, activeElement: {
            blur: function () {
            }, nodeName: ""
        }, querySelector: function () {
            return null
        }, querySelectorAll: function () {
            return []
        }, getElementById: function () {
            return null
        }, createEvent: function () {
            return {
                initEvent: function () {
                }
            }
        }, createElement: function () {
            return {
                children: [], childNodes: [], style: {}, setAttribute: function () {
                }, getElementsByTagName: function () {
                    return []
                }
            }
        }, location: {hash: ""}
    } : document, Y = "undefined" == typeof window ? {
        document: f,
        navigator: {userAgent: ""},
        location: {},
        history: {},
        CustomEvent: function () {
            return this
        },
        addEventListener: function () {
        },
        removeEventListener: function () {
        },
        getComputedStyle: function () {
            return {
                getPropertyValue: function () {
                    return ""
                }
            }
        },
        Image: function () {
        },
        Date: function () {
        },
        screen: {},
        setTimeout: function () {
        },
        clearTimeout: function () {
        }
    } : window, l = function (e) {
        for (var t = 0; t < e.length; t += 1) this[t] = e[t];
        return this.length = e.length, this
    };

    function L(e, t) {
        var a = [], i = 0;
        if (e && !t && e instanceof l) return e;
        if (e) if ("string" == typeof e) {
            var s, r, n = e.trim();
            if (0 <= n.indexOf("<") && 0 <= n.indexOf(">")) {
                var o = "div";
                for (0 === n.indexOf("<li") && (o = "ul"), 0 === n.indexOf("<tr") && (o = "tbody"), 0 !== n.indexOf("<td") && 0 !== n.indexOf("<th") || (o = "tr"), 0 === n.indexOf("<tbody") && (o = "table"), 0 === n.indexOf("<option") && (o = "select"), (r = f.createElement(o)).innerHTML = n, i = 0; i < r.childNodes.length; i += 1) a.push(r.childNodes[i])
            } else for (s = t || "#" !== e[0] || e.match(/[ .<>:~]/) ? (t || f).querySelectorAll(e.trim()) : [f.getElementById(e.trim().split("#")[1])], i = 0; i < s.length; i += 1) s[i] && a.push(s[i])
        } else if (e.nodeType || e === Y || e === f) a.push(e); else if (0 < e.length && e[0].nodeType) for (i = 0; i < e.length; i += 1) a.push(e[i]);
        return new l(a)
    }

    function r(e) {
        for (var t = [], a = 0; a < e.length; a += 1) -1 === t.indexOf(e[a]) && t.push(e[a]);
        return t
    }

    L.fn = l.prototype, L.Class = l, L.Dom7 = l;
    var t = {
        addClass: function (e) {
            if (void 0 === e) return this;
            for (var t = e.split(" "), a = 0; a < t.length; a += 1) for (var i = 0; i < this.length; i += 1) void 0 !== this[i] && void 0 !== this[i].classList && this[i].classList.add(t[a]);
            return this
        }, removeClass: function (e) {
            for (var t = e.split(" "), a = 0; a < t.length; a += 1) for (var i = 0; i < this.length; i += 1) void 0 !== this[i] && void 0 !== this[i].classList && this[i].classList.remove(t[a]);
            return this
        }, hasClass: function (e) {
            return !!this[0] && this[0].classList.contains(e)
        }, toggleClass: function (e) {
            for (var t = e.split(" "), a = 0; a < t.length; a += 1) for (var i = 0; i < this.length; i += 1) void 0 !== this[i] && void 0 !== this[i].classList && this[i].classList.toggle(t[a]);
            return this
        }, attr: function (e, t) {
            var a = arguments;
            if (1 === arguments.length && "string" == typeof e) return this[0] ? this[0].getAttribute(e) : void 0;
            for (var i = 0; i < this.length; i += 1) if (2 === a.length) this[i].setAttribute(e, t); else for (var s in e) this[i][s] = e[s], this[i].setAttribute(s, e[s]);
            return this
        }, removeAttr: function (e) {
            for (var t = 0; t < this.length; t += 1) this[t].removeAttribute(e);
            return this
        }, data: function (e, t) {
            var a;
            if (void 0 !== t) {
                for (var i = 0; i < this.length; i += 1) (a = this[i]).dom7ElementDataStorage || (a.dom7ElementDataStorage = {}), a.dom7ElementDataStorage[e] = t;
                return this
            }
            if (a = this[0]) {
                if (a.dom7ElementDataStorage && e in a.dom7ElementDataStorage) return a.dom7ElementDataStorage[e];
                var s = a.getAttribute("data-" + e);
                return s || void 0
            }
        }, transform: function (e) {
            for (var t = 0; t < this.length; t += 1) {
                var a = this[t].style;
                a.webkitTransform = e, a.transform = e
            }
            return this
        }, transition: function (e) {
            "string" != typeof e && (e += "ms");
            for (var t = 0; t < this.length; t += 1) {
                var a = this[t].style;
                a.webkitTransitionDuration = e, a.transitionDuration = e
            }
            return this
        }, on: function () {
            for (var e, t = [], a = arguments.length; a--;) t[a] = arguments[a];
            var i = t[0], r = t[1], n = t[2], s = t[3];

            function o(e) {
                var t = e.target;
                if (t) {
                    var a = e.target.dom7EventData || [];
                    if (a.indexOf(e) < 0 && a.unshift(e), L(t).is(r)) n.apply(t, a); else for (var i = L(t).parents(), s = 0; s < i.length; s += 1) L(i[s]).is(r) && n.apply(i[s], a)
                }
            }

            function l(e) {
                var t = e && e.target && e.target.dom7EventData || [];
                t.indexOf(e) < 0 && t.unshift(e), n.apply(this, t)
            }

            "function" == typeof t[1] && (i = (e = t)[0], n = e[1], s = e[2], r = void 0), s || (s = !1);
            for (var d, p = i.split(" "), c = 0; c < this.length; c += 1) {
                var u = this[c];
                if (r) for (d = 0; d < p.length; d += 1) {
                    var h = p[d];
                    u.dom7LiveListeners || (u.dom7LiveListeners = {}), u.dom7LiveListeners[h] || (u.dom7LiveListeners[h] = []), u.dom7LiveListeners[h].push({
                        listener: n,
                        proxyListener: o
                    }), u.addEventListener(h, o, s)
                } else for (d = 0; d < p.length; d += 1) {
                    var v = p[d];
                    u.dom7Listeners || (u.dom7Listeners = {}), u.dom7Listeners[v] || (u.dom7Listeners[v] = []), u.dom7Listeners[v].push({
                        listener: n,
                        proxyListener: l
                    }), u.addEventListener(v, l, s)
                }
            }
            return this
        }, off: function () {
            for (var e, t = [], a = arguments.length; a--;) t[a] = arguments[a];
            var i = t[0], s = t[1], r = t[2], n = t[3];
            "function" == typeof t[1] && (i = (e = t)[0], r = e[1], n = e[2], s = void 0), n || (n = !1);
            for (var o = i.split(" "), l = 0; l < o.length; l += 1) for (var d = o[l], p = 0; p < this.length; p += 1) {
                var c = this[p], u = void 0;
                if (!s && c.dom7Listeners ? u = c.dom7Listeners[d] : s && c.dom7LiveListeners && (u = c.dom7LiveListeners[d]), u && u.length) for (var h = u.length - 1; 0 <= h; h -= 1) {
                    var v = u[h];
                    r && v.listener === r ? (c.removeEventListener(d, v.proxyListener, n), u.splice(h, 1)) : r || (c.removeEventListener(d, v.proxyListener, n), u.splice(h, 1))
                }
            }
            return this
        }, trigger: function () {
            for (var e = [], t = arguments.length; t--;) e[t] = arguments[t];
            for (var a = e[0].split(" "), i = e[1], s = 0; s < a.length; s += 1) for (var r = a[s], n = 0; n < this.length; n += 1) {
                var o = this[n], l = void 0;
                try {
                    l = new Y.CustomEvent(r, {detail: i, bubbles: !0, cancelable: !0})
                } catch (e) {
                    (l = f.createEvent("Event")).initEvent(r, !0, !0), l.detail = i
                }
                o.dom7EventData = e.filter(function (e, t) {
                    return 0 < t
                }), o.dispatchEvent(l), o.dom7EventData = [], delete o.dom7EventData
            }
            return this
        }, transitionEnd: function (t) {
            var a, i = ["webkitTransitionEnd", "transitionend"], s = this;

            function r(e) {
                if (e.target === this) for (t.call(this, e), a = 0; a < i.length; a += 1) s.off(i[a], r)
            }

            if (t) for (a = 0; a < i.length; a += 1) s.on(i[a], r);
            return this
        }, outerWidth: function (e) {
            if (0 < this.length) {
                if (e) {
                    var t = this.styles();
                    return this[0].offsetWidth + parseFloat(t.getPropertyValue("margin-right")) + parseFloat(t.getPropertyValue("margin-left"))
                }
                return this[0].offsetWidth
            }
            return null
        }, outerHeight: function (e) {
            if (0 < this.length) {
                if (e) {
                    var t = this.styles();
                    return this[0].offsetHeight + parseFloat(t.getPropertyValue("margin-top")) + parseFloat(t.getPropertyValue("margin-bottom"))
                }
                return this[0].offsetHeight
            }
            return null
        }, offset: function () {
            if (0 < this.length) {
                var e = this[0], t = e.getBoundingClientRect(), a = f.body, i = e.clientTop || a.clientTop || 0,
                    s = e.clientLeft || a.clientLeft || 0, r = e === Y ? Y.scrollY : e.scrollTop,
                    n = e === Y ? Y.scrollX : e.scrollLeft;
                return {top: t.top + r - i, left: t.left + n - s}
            }
            return null
        }, css: function (e, t) {
            var a;
            if (1 === arguments.length) {
                if ("string" != typeof e) {
                    for (a = 0; a < this.length; a += 1) for (var i in e) this[a].style[i] = e[i];
                    return this
                }
                if (this[0]) return Y.getComputedStyle(this[0], null).getPropertyValue(e)
            }
            if (2 === arguments.length && "string" == typeof e) {
                for (a = 0; a < this.length; a += 1) this[a].style[e] = t;
                return this
            }
            return this
        }, each: function (e) {
            if (!e) return this;
            for (var t = 0; t < this.length; t += 1) if (!1 === e.call(this[t], t, this[t])) return this;
            return this
        }, html: function (e) {
            if (void 0 === e) return this[0] ? this[0].innerHTML : void 0;
            for (var t = 0; t < this.length; t += 1) this[t].innerHTML = e;
            return this
        }, text: function (e) {
            if (void 0 === e) return this[0] ? this[0].textContent.trim() : null;
            for (var t = 0; t < this.length; t += 1) this[t].textContent = e;
            return this
        }, is: function (e) {
            var t, a, i = this[0];
            if (!i || void 0 === e) return !1;
            if ("string" == typeof e) {
                if (i.matches) return i.matches(e);
                if (i.webkitMatchesSelector) return i.webkitMatchesSelector(e);
                if (i.msMatchesSelector) return i.msMatchesSelector(e);
                for (t = L(e), a = 0; a < t.length; a += 1) if (t[a] === i) return !0;
                return !1
            }
            if (e === f) return i === f;
            if (e === Y) return i === Y;
            if (e.nodeType || e instanceof l) {
                for (t = e.nodeType ? [e] : e, a = 0; a < t.length; a += 1) if (t[a] === i) return !0;
                return !1
            }
            return !1
        }, index: function () {
            var e, t = this[0];
            if (t) {
                for (e = 0; null !== (t = t.previousSibling);) 1 === t.nodeType && (e += 1);
                return e
            }
        }, eq: function (e) {
            if (void 0 === e) return this;
            var t, a = this.length;
            return new l(a - 1 < e ? [] : e < 0 ? (t = a + e) < 0 ? [] : [this[t]] : [this[e]])
        }, append: function () {
            for (var e, t = [], a = arguments.length; a--;) t[a] = arguments[a];
            for (var i = 0; i < t.length; i += 1) {
                e = t[i];
                for (var s = 0; s < this.length; s += 1) if ("string" == typeof e) {
                    var r = f.createElement("div");
                    for (r.innerHTML = e; r.firstChild;) this[s].appendChild(r.firstChild)
                } else if (e instanceof l) for (var n = 0; n < e.length; n += 1) this[s].appendChild(e[n]); else this[s].appendChild(e)
            }
            return this
        }, prepend: function (e) {
            var t, a, i = this;
            for (t = 0; t < this.length; t += 1) if ("string" == typeof e) {
                var s = f.createElement("div");
                for (s.innerHTML = e, a = s.childNodes.length - 1; 0 <= a; a -= 1) i[t].insertBefore(s.childNodes[a], i[t].childNodes[0])
            } else if (e instanceof l) for (a = 0; a < e.length; a += 1) i[t].insertBefore(e[a], i[t].childNodes[0]); else i[t].insertBefore(e, i[t].childNodes[0]);
            return this
        }, next: function (e) {
            return 0 < this.length ? e ? this[0].nextElementSibling && L(this[0].nextElementSibling).is(e) ? new l([this[0].nextElementSibling]) : new l([]) : this[0].nextElementSibling ? new l([this[0].nextElementSibling]) : new l([]) : new l([])
        }, nextAll: function (e) {
            var t = [], a = this[0];
            if (!a) return new l([]);
            for (; a.nextElementSibling;) {
                var i = a.nextElementSibling;
                e ? L(i).is(e) && t.push(i) : t.push(i), a = i
            }
            return new l(t)
        }, prev: function (e) {
            if (0 < this.length) {
                var t = this[0];
                return e ? t.previousElementSibling && L(t.previousElementSibling).is(e) ? new l([t.previousElementSibling]) : new l([]) : t.previousElementSibling ? new l([t.previousElementSibling]) : new l([])
            }
            return new l([])
        }, prevAll: function (e) {
            var t = [], a = this[0];
            if (!a) return new l([]);
            for (; a.previousElementSibling;) {
                var i = a.previousElementSibling;
                e ? L(i).is(e) && t.push(i) : t.push(i), a = i
            }
            return new l(t)
        }, parent: function (e) {
            for (var t = [], a = 0; a < this.length; a += 1) null !== this[a].parentNode && (e ? L(this[a].parentNode).is(e) && t.push(this[a].parentNode) : t.push(this[a].parentNode));
            return L(r(t))
        }, parents: function (e) {
            for (var t = [], a = 0; a < this.length; a += 1) for (var i = this[a].parentNode; i;) e ? L(i).is(e) && t.push(i) : t.push(i), i = i.parentNode;
            return L(r(t))
        }, closest: function (e) {
            var t = this;
            return void 0 === e ? new l([]) : (t.is(e) || (t = t.parents(e).eq(0)), t)
        }, find: function (e) {
            for (var t = [], a = 0; a < this.length; a += 1) for (var i = this[a].querySelectorAll(e), s = 0; s < i.length; s += 1) t.push(i[s]);
            return new l(t)
        }, children: function (e) {
            for (var t = [], a = 0; a < this.length; a += 1) for (var i = this[a].childNodes, s = 0; s < i.length; s += 1) e ? 1 === i[s].nodeType && L(i[s]).is(e) && t.push(i[s]) : 1 === i[s].nodeType && t.push(i[s]);
            return new l(r(t))
        }, remove: function () {
            for (var e = 0; e < this.length; e += 1) this[e].parentNode && this[e].parentNode.removeChild(this[e]);
            return this
        }, add: function () {
            for (var e = [], t = arguments.length; t--;) e[t] = arguments[t];
            var a, i;
            for (a = 0; a < e.length; a += 1) {
                var s = L(e[a]);
                for (i = 0; i < s.length; i += 1) this[this.length] = s[i], this.length += 1
            }
            return this
        }, styles: function () {
            return this[0] ? Y.getComputedStyle(this[0], null) : {}
        }
    };
    Object.keys(t).forEach(function (e) {
        L.fn[e] = t[e]
    });
    var e, a, i, V = {
        deleteProps: function (e) {
            var t = e;
            Object.keys(t).forEach(function (e) {
                try {
                    t[e] = null
                } catch (e) {
                }
                try {
                    delete t[e]
                } catch (e) {
                }
            })
        }, nextTick: function (e, t) {
            return void 0 === t && (t = 0), setTimeout(e, t)
        }, now: function () {
            return Date.now()
        }, getTranslate: function (e, t) {
            var a, i, s;
            void 0 === t && (t = "x");
            var r = Y.getComputedStyle(e, null);
            return Y.WebKitCSSMatrix ? (6 < (i = r.transform || r.webkitTransform).split(",").length && (i = i.split(", ").map(function (e) {
                return e.replace(",", ".")
            }).join(", ")), s = new Y.WebKitCSSMatrix("none" === i ? "" : i)) : a = (s = r.MozTransform || r.OTransform || r.MsTransform || r.msTransform || r.transform || r.getPropertyValue("transform").replace("translate(", "matrix(1, 0, 0, 1,")).toString().split(","), "x" === t && (i = Y.WebKitCSSMatrix ? s.m41 : 16 === a.length ? parseFloat(a[12]) : parseFloat(a[4])), "y" === t && (i = Y.WebKitCSSMatrix ? s.m42 : 16 === a.length ? parseFloat(a[13]) : parseFloat(a[5])), i || 0
        }, parseUrlQuery: function (e) {
            var t, a, i, s, r = {}, n = e || Y.location.href;
            if ("string" == typeof n && n.length) for (s = (a = (n = -1 < n.indexOf("?") ? n.replace(/\S*\?/, "") : "").split("&").filter(function (e) {
                return "" !== e
            })).length, t = 0; t < s; t += 1) i = a[t].replace(/#\S+/g, "").split("="), r[decodeURIComponent(i[0])] = void 0 === i[1] ? void 0 : decodeURIComponent(i[1]) || "";
            return r
        }, isObject: function (e) {
            return "object" == typeof e && null !== e && e.constructor && e.constructor === Object
        }, extend: function () {
            for (var e = [], t = arguments.length; t--;) e[t] = arguments[t];
            for (var a = Object(e[0]), i = 1; i < e.length; i += 1) {
                var s = e[i];
                if (null != s) for (var r = Object.keys(Object(s)), n = 0, o = r.length; n < o; n += 1) {
                    var l = r[n], d = Object.getOwnPropertyDescriptor(s, l);
                    void 0 !== d && d.enumerable && (V.isObject(a[l]) && V.isObject(s[l]) ? V.extend(a[l], s[l]) : !V.isObject(a[l]) && V.isObject(s[l]) ? (a[l] = {}, V.extend(a[l], s[l])) : a[l] = s[l])
                }
            }
            return a
        }
    }, R = (i = f.createElement("div"), {
        touch: Y.Modernizr && !0 === Y.Modernizr.touch || !!("ontouchstart" in Y || Y.DocumentTouch && f instanceof Y.DocumentTouch),
        pointerEvents: !(!Y.navigator.pointerEnabled && !Y.PointerEvent),
        prefixedPointerEvents: !!Y.navigator.msPointerEnabled,
        transition: (a = i.style, "transition" in a || "webkitTransition" in a || "MozTransition" in a),
        transforms3d: Y.Modernizr && !0 === Y.Modernizr.csstransforms3d || (e = i.style, "webkitPerspective" in e || "MozPerspective" in e || "OPerspective" in e || "MsPerspective" in e || "perspective" in e),
        flexbox: function () {
            for (var e = i.style, t = "alignItems webkitAlignItems webkitBoxAlign msFlexAlign mozBoxAlign webkitFlexDirection msFlexDirection mozBoxDirection mozBoxOrient webkitBoxDirection webkitBoxOrient".split(" "), a = 0; a < t.length; a += 1) if (t[a] in e) return !0;
            return !1
        }(),
        observer: "MutationObserver" in Y || "WebkitMutationObserver" in Y,
        passiveListener: function () {
            var e = !1;
            try {
                var t = Object.defineProperty({}, "passive", {
                    get: function () {
                        e = !0
                    }
                });
                Y.addEventListener("testPassiveListener", null, t)
            } catch (e) {
            }
            return e
        }(),
        gestures: "ongesturestart" in Y
    }), s = function (e) {
        void 0 === e && (e = {});
        var t = this;
        t.params = e, t.eventsListeners = {}, t.params && t.params.on && Object.keys(t.params.on).forEach(function (e) {
            t.on(e, t.params.on[e])
        })
    }, n = {components: {configurable: !0}};
    s.prototype.on = function (e, t, a) {
        var i = this;
        if ("function" != typeof t) return i;
        var s = a ? "unshift" : "push";
        return e.split(" ").forEach(function (e) {
            i.eventsListeners[e] || (i.eventsListeners[e] = []), i.eventsListeners[e][s](t)
        }), i
    }, s.prototype.once = function (i, s, e) {
        var r = this;
        if ("function" != typeof s) return r;
        return r.on(i, function e() {
            for (var t = [], a = arguments.length; a--;) t[a] = arguments[a];
            s.apply(r, t), r.off(i, e)
        }, e)
    }, s.prototype.off = function (e, i) {
        var s = this;
        return s.eventsListeners && e.split(" ").forEach(function (a) {
            void 0 === i ? s.eventsListeners[a] = [] : s.eventsListeners[a] && s.eventsListeners[a].length && s.eventsListeners[a].forEach(function (e, t) {
                e === i && s.eventsListeners[a].splice(t, 1)
            })
        }), s
    }, s.prototype.emit = function () {
        for (var e = [], t = arguments.length; t--;) e[t] = arguments[t];
        var a, i, s, r = this;
        return r.eventsListeners && ("string" == typeof e[0] || Array.isArray(e[0]) ? (a = e[0], i = e.slice(1, e.length), s = r) : (a = e[0].events, i = e[0].data, s = e[0].context || r), (Array.isArray(a) ? a : a.split(" ")).forEach(function (e) {
            if (r.eventsListeners && r.eventsListeners[e]) {
                var t = [];
                r.eventsListeners[e].forEach(function (e) {
                    t.push(e)
                }), t.forEach(function (e) {
                    e.apply(s, i)
                })
            }
        })), r
    }, s.prototype.useModulesParams = function (a) {
        var i = this;
        i.modules && Object.keys(i.modules).forEach(function (e) {
            var t = i.modules[e];
            t.params && V.extend(a, t.params)
        })
    }, s.prototype.useModules = function (i) {
        void 0 === i && (i = {});
        var s = this;
        s.modules && Object.keys(s.modules).forEach(function (e) {
            var a = s.modules[e], t = i[e] || {};
            a.instance && Object.keys(a.instance).forEach(function (e) {
                var t = a.instance[e];
                s[e] = "function" == typeof t ? t.bind(s) : t
            }), a.on && s.on && Object.keys(a.on).forEach(function (e) {
                s.on(e, a.on[e])
            }), a.create && a.create.bind(s)(t)
        })
    }, n.components.set = function (e) {
        this.use && this.use(e)
    }, s.installModule = function (t) {
        for (var e = [], a = arguments.length - 1; 0 < a--;) e[a] = arguments[a + 1];
        var i = this;
        i.prototype.modules || (i.prototype.modules = {});
        var s = t.name || Object.keys(i.prototype.modules).length + "_" + V.now();
        return (i.prototype.modules[s] = t).proto && Object.keys(t.proto).forEach(function (e) {
            i.prototype[e] = t.proto[e]
        }), t.static && Object.keys(t.static).forEach(function (e) {
            i[e] = t.static[e]
        }), t.install && t.install.apply(i, e), i
    }, s.use = function (e) {
        for (var t = [], a = arguments.length - 1; 0 < a--;) t[a] = arguments[a + 1];
        var i = this;
        return Array.isArray(e) ? (e.forEach(function (e) {
            return i.installModule(e)
        }), i) : i.installModule.apply(i, [e].concat(t))
    }, Object.defineProperties(s, n);
    var o = {
        updateSize: function () {
            var e, t, a = this, i = a.$el;
            e = void 0 !== a.params.width ? a.params.width : i[0].clientWidth, t = void 0 !== a.params.height ? a.params.height : i[0].clientHeight, 0 === e && a.isHorizontal() || 0 === t && a.isVertical() || (e = e - parseInt(i.css("padding-left"), 10) - parseInt(i.css("padding-right"), 10), t = t - parseInt(i.css("padding-top"), 10) - parseInt(i.css("padding-bottom"), 10), V.extend(a, {
                width: e,
                height: t,
                size: a.isHorizontal() ? e : t
            }))
        }, updateSlides: function () {
            var e = this, t = e.params, a = e.$wrapperEl, i = e.size, s = e.rtlTranslate, r = e.wrongRTL,
                n = e.virtual && t.virtual.enabled, o = n ? e.virtual.slides.length : e.slides.length,
                l = a.children("." + e.params.slideClass), d = n ? e.virtual.slides.length : l.length, p = [], c = [],
                u = [], h = t.slidesOffsetBefore;
            "function" == typeof h && (h = t.slidesOffsetBefore.call(e));
            var v = t.slidesOffsetAfter;
            "function" == typeof v && (v = t.slidesOffsetAfter.call(e));
            var f = e.snapGrid.length, m = e.snapGrid.length, g = t.spaceBetween, b = -h, w = 0, y = 0;
            if (void 0 !== i) {
                var x, T;
                "string" == typeof g && 0 <= g.indexOf("%") && (g = parseFloat(g.replace("%", "")) / 100 * i), e.virtualSize = -g, s ? l.css({
                    marginLeft: "",
                    marginTop: ""
                }) : l.css({
                    marginRight: "",
                    marginBottom: ""
                }), 1 < t.slidesPerColumn && (x = Math.floor(d / t.slidesPerColumn) === d / e.params.slidesPerColumn ? d : Math.ceil(d / t.slidesPerColumn) * t.slidesPerColumn, "auto" !== t.slidesPerView && "row" === t.slidesPerColumnFill && (x = Math.max(x, t.slidesPerView * t.slidesPerColumn)));
                for (var E, S = t.slidesPerColumn, C = x / S, M = C - (t.slidesPerColumn * C - d), k = 0; k < d; k += 1) {
                    T = 0;
                    var z = l.eq(k);
                    if (1 < t.slidesPerColumn) {
                        var P = void 0, $ = void 0, L = void 0;
                        "column" === t.slidesPerColumnFill ? (L = k - ($ = Math.floor(k / S)) * S, (M < $ || $ === M && L === S - 1) && S <= (L += 1) && (L = 0, $ += 1), P = $ + L * x / S, z.css({
                            "-webkit-box-ordinal-group": P,
                            "-moz-box-ordinal-group": P,
                            "-ms-flex-order": P,
                            "-webkit-order": P,
                            order: P
                        })) : $ = k - (L = Math.floor(k / C)) * C, z.css("margin-" + (e.isHorizontal() ? "top" : "left"), 0 !== L && t.spaceBetween && t.spaceBetween + "px").attr("data-swiper-column", $).attr("data-swiper-row", L)
                    }
                    if ("none" !== z.css("display")) {
                        if ("auto" === t.slidesPerView) {
                            var I = Y.getComputedStyle(z[0], null), D = z[0].style.transform,
                                O = z[0].style.webkitTransform;
                            D && (z[0].style.transform = "none"), O && (z[0].style.webkitTransform = "none"), T = t.roundLengths ? e.isHorizontal() ? z.outerWidth(!0) : z.outerHeight(!0) : e.isHorizontal() ? z[0].getBoundingClientRect().width + parseFloat(I.getPropertyValue("margin-left")) + parseFloat(I.getPropertyValue("margin-right")) : z[0].getBoundingClientRect().height + parseFloat(I.getPropertyValue("margin-top")) + parseFloat(I.getPropertyValue("margin-bottom")), D && (z[0].style.transform = D), O && (z[0].style.webkitTransform = O), t.roundLengths && (T = Math.floor(T))
                        } else T = (i - (t.slidesPerView - 1) * g) / t.slidesPerView, t.roundLengths && (T = Math.floor(T)), l[k] && (e.isHorizontal() ? l[k].style.width = T + "px" : l[k].style.height = T + "px");
                        l[k] && (l[k].swiperSlideSize = T), u.push(T), t.centeredSlides ? (b = b + T / 2 + w / 2 + g, 0 === w && 0 !== k && (b = b - i / 2 - g), 0 === k && (b = b - i / 2 - g), Math.abs(b) < .001 && (b = 0), t.roundLengths && (b = Math.floor(b)), y % t.slidesPerGroup == 0 && p.push(b), c.push(b)) : (t.roundLengths && (b = Math.floor(b)), y % t.slidesPerGroup == 0 && p.push(b), c.push(b), b = b + T + g), e.virtualSize += T + g, w = T, y += 1
                    }
                }
                if (e.virtualSize = Math.max(e.virtualSize, i) + v, s && r && ("slide" === t.effect || "coverflow" === t.effect) && a.css({width: e.virtualSize + t.spaceBetween + "px"}), R.flexbox && !t.setWrapperSize || (e.isHorizontal() ? a.css({width: e.virtualSize + t.spaceBetween + "px"}) : a.css({height: e.virtualSize + t.spaceBetween + "px"})), 1 < t.slidesPerColumn && (e.virtualSize = (T + t.spaceBetween) * x, e.virtualSize = Math.ceil(e.virtualSize / t.slidesPerColumn) - t.spaceBetween, e.isHorizontal() ? a.css({width: e.virtualSize + t.spaceBetween + "px"}) : a.css({height: e.virtualSize + t.spaceBetween + "px"}), t.centeredSlides)) {
                    E = [];
                    for (var A = 0; A < p.length; A += 1) {
                        var H = p[A];
                        t.roundLengths && (H = Math.floor(H)), p[A] < e.virtualSize + p[0] && E.push(H)
                    }
                    p = E
                }
                if (!t.centeredSlides) {
                    E = [];
                    for (var B = 0; B < p.length; B += 1) {
                        var G = p[B];
                        t.roundLengths && (G = Math.floor(G)), p[B] <= e.virtualSize - i && E.push(G)
                    }
                    p = E, 1 < Math.floor(e.virtualSize - i) - Math.floor(p[p.length - 1]) && p.push(e.virtualSize - i)
                }
                if (0 === p.length && (p = [0]), 0 !== t.spaceBetween && (e.isHorizontal() ? s ? l.css({marginLeft: g + "px"}) : l.css({marginRight: g + "px"}) : l.css({marginBottom: g + "px"})), t.centerInsufficientSlides) {
                    var N = 0;
                    if (u.forEach(function (e) {
                        N += e + (t.spaceBetween ? t.spaceBetween : 0)
                    }), (N -= t.spaceBetween) < i) {
                        var X = (i - N) / 2;
                        p.forEach(function (e, t) {
                            p[t] = e - X
                        }), c.forEach(function (e, t) {
                            c[t] = e + X
                        })
                    }
                }
                V.extend(e, {
                    slides: l,
                    snapGrid: p,
                    slidesGrid: c,
                    slidesSizesGrid: u
                }), d !== o && e.emit("slidesLengthChange"), p.length !== f && (e.params.watchOverflow && e.checkOverflow(), e.emit("snapGridLengthChange")), c.length !== m && e.emit("slidesGridLengthChange"), (t.watchSlidesProgress || t.watchSlidesVisibility) && e.updateSlidesOffset()
            }
        }, updateAutoHeight: function (e) {
            var t, a = this, i = [], s = 0;
            if ("number" == typeof e ? a.setTransition(e) : !0 === e && a.setTransition(a.params.speed), "auto" !== a.params.slidesPerView && 1 < a.params.slidesPerView) for (t = 0; t < Math.ceil(a.params.slidesPerView); t += 1) {
                var r = a.activeIndex + t;
                if (r > a.slides.length) break;
                i.push(a.slides.eq(r)[0])
            } else i.push(a.slides.eq(a.activeIndex)[0]);
            for (t = 0; t < i.length; t += 1) if (void 0 !== i[t]) {
                var n = i[t].offsetHeight;
                s = s < n ? n : s
            }
            s && a.$wrapperEl.css("height", s + "px")
        }, updateSlidesOffset: function () {
            for (var e = this.slides, t = 0; t < e.length; t += 1) e[t].swiperSlideOffset = this.isHorizontal() ? e[t].offsetLeft : e[t].offsetTop
        }, updateSlidesProgress: function (e) {
            void 0 === e && (e = this && this.translate || 0);
            var t = this, a = t.params, i = t.slides, s = t.rtlTranslate;
            if (0 !== i.length) {
                void 0 === i[0].swiperSlideOffset && t.updateSlidesOffset();
                var r = -e;
                s && (r = e), i.removeClass(a.slideVisibleClass), t.visibleSlidesIndexes = [], t.visibleSlides = [];
                for (var n = 0; n < i.length; n += 1) {
                    var o = i[n],
                        l = (r + (a.centeredSlides ? t.minTranslate() : 0) - o.swiperSlideOffset) / (o.swiperSlideSize + a.spaceBetween);
                    if (a.watchSlidesVisibility) {
                        var d = -(r - o.swiperSlideOffset), p = d + t.slidesSizesGrid[n];
                        (0 <= d && d < t.size || 0 < p && p <= t.size || d <= 0 && p >= t.size) && (t.visibleSlides.push(o), t.visibleSlidesIndexes.push(n), i.eq(n).addClass(a.slideVisibleClass))
                    }
                    o.progress = s ? -l : l
                }
                t.visibleSlides = L(t.visibleSlides)
            }
        }, updateProgress: function (e) {
            void 0 === e && (e = this && this.translate || 0);
            var t = this, a = t.params, i = t.maxTranslate() - t.minTranslate(), s = t.progress, r = t.isBeginning,
                n = t.isEnd, o = r, l = n;
            0 === i ? n = r = !(s = 0) : (r = (s = (e - t.minTranslate()) / i) <= 0, n = 1 <= s), V.extend(t, {
                progress: s,
                isBeginning: r,
                isEnd: n
            }), (a.watchSlidesProgress || a.watchSlidesVisibility) && t.updateSlidesProgress(e), r && !o && t.emit("reachBeginning toEdge"), n && !l && t.emit("reachEnd toEdge"), (o && !r || l && !n) && t.emit("fromEdge"), t.emit("progress", s)
        }, updateSlidesClasses: function () {
            var e, t = this, a = t.slides, i = t.params, s = t.$wrapperEl, r = t.activeIndex, n = t.realIndex,
                o = t.virtual && i.virtual.enabled;
            a.removeClass(i.slideActiveClass + " " + i.slideNextClass + " " + i.slidePrevClass + " " + i.slideDuplicateActiveClass + " " + i.slideDuplicateNextClass + " " + i.slideDuplicatePrevClass), (e = o ? t.$wrapperEl.find("." + i.slideClass + '[data-swiper-slide-index="' + r + '"]') : a.eq(r)).addClass(i.slideActiveClass), i.loop && (e.hasClass(i.slideDuplicateClass) ? s.children("." + i.slideClass + ":not(." + i.slideDuplicateClass + ')[data-swiper-slide-index="' + n + '"]').addClass(i.slideDuplicateActiveClass) : s.children("." + i.slideClass + "." + i.slideDuplicateClass + '[data-swiper-slide-index="' + n + '"]').addClass(i.slideDuplicateActiveClass));
            var l = e.nextAll("." + i.slideClass).eq(0).addClass(i.slideNextClass);
            i.loop && 0 === l.length && (l = a.eq(0)).addClass(i.slideNextClass);
            var d = e.prevAll("." + i.slideClass).eq(0).addClass(i.slidePrevClass);
            i.loop && 0 === d.length && (d = a.eq(-1)).addClass(i.slidePrevClass), i.loop && (l.hasClass(i.slideDuplicateClass) ? s.children("." + i.slideClass + ":not(." + i.slideDuplicateClass + ')[data-swiper-slide-index="' + l.attr("data-swiper-slide-index") + '"]').addClass(i.slideDuplicateNextClass) : s.children("." + i.slideClass + "." + i.slideDuplicateClass + '[data-swiper-slide-index="' + l.attr("data-swiper-slide-index") + '"]').addClass(i.slideDuplicateNextClass), d.hasClass(i.slideDuplicateClass) ? s.children("." + i.slideClass + ":not(." + i.slideDuplicateClass + ')[data-swiper-slide-index="' + d.attr("data-swiper-slide-index") + '"]').addClass(i.slideDuplicatePrevClass) : s.children("." + i.slideClass + "." + i.slideDuplicateClass + '[data-swiper-slide-index="' + d.attr("data-swiper-slide-index") + '"]').addClass(i.slideDuplicatePrevClass))
        }, updateActiveIndex: function (e) {
            var t, a = this, i = a.rtlTranslate ? a.translate : -a.translate, s = a.slidesGrid, r = a.snapGrid,
                n = a.params, o = a.activeIndex, l = a.realIndex, d = a.snapIndex, p = e;
            if (void 0 === p) {
                for (var c = 0; c < s.length; c += 1) void 0 !== s[c + 1] ? i >= s[c] && i < s[c + 1] - (s[c + 1] - s[c]) / 2 ? p = c : i >= s[c] && i < s[c + 1] && (p = c + 1) : i >= s[c] && (p = c);
                n.normalizeSlideIndex && (p < 0 || void 0 === p) && (p = 0)
            }
            if ((t = 0 <= r.indexOf(i) ? r.indexOf(i) : Math.floor(p / n.slidesPerGroup)) >= r.length && (t = r.length - 1), p !== o) {
                var u = parseInt(a.slides.eq(p).attr("data-swiper-slide-index") || p, 10);
                V.extend(a, {
                    snapIndex: t,
                    realIndex: u,
                    previousIndex: o,
                    activeIndex: p
                }), a.emit("activeIndexChange"), a.emit("snapIndexChange"), l !== u && a.emit("realIndexChange"), a.emit("slideChange")
            } else t !== d && (a.snapIndex = t, a.emit("snapIndexChange"))
        }, updateClickedSlide: function (e) {
            var t = this, a = t.params, i = L(e.target).closest("." + a.slideClass)[0], s = !1;
            if (i) for (var r = 0; r < t.slides.length; r += 1) t.slides[r] === i && (s = !0);
            if (!i || !s) return t.clickedSlide = void 0, void (t.clickedIndex = void 0);
            t.clickedSlide = i, t.virtual && t.params.virtual.enabled ? t.clickedIndex = parseInt(L(i).attr("data-swiper-slide-index"), 10) : t.clickedIndex = L(i).index(), a.slideToClickedSlide && void 0 !== t.clickedIndex && t.clickedIndex !== t.activeIndex && t.slideToClickedSlide()
        }
    };
    var d = {
        getTranslate: function (e) {
            void 0 === e && (e = this.isHorizontal() ? "x" : "y");
            var t = this.params, a = this.rtlTranslate, i = this.translate, s = this.$wrapperEl;
            if (t.virtualTranslate) return a ? -i : i;
            var r = V.getTranslate(s[0], e);
            return a && (r = -r), r || 0
        }, setTranslate: function (e, t) {
            var a = this, i = a.rtlTranslate, s = a.params, r = a.$wrapperEl, n = a.progress, o = 0, l = 0;
            a.isHorizontal() ? o = i ? -e : e : l = e, s.roundLengths && (o = Math.floor(o), l = Math.floor(l)), s.virtualTranslate || (R.transforms3d ? r.transform("translate3d(" + o + "px, " + l + "px, 0px)") : r.transform("translate(" + o + "px, " + l + "px)")), a.previousTranslate = a.translate, a.translate = a.isHorizontal() ? o : l;
            var d = a.maxTranslate() - a.minTranslate();
            (0 === d ? 0 : (e - a.minTranslate()) / d) !== n && a.updateProgress(e), a.emit("setTranslate", a.translate, t)
        }, minTranslate: function () {
            return -this.snapGrid[0]
        }, maxTranslate: function () {
            return -this.snapGrid[this.snapGrid.length - 1]
        }
    };
    var p = {
        setTransition: function (e, t) {
            this.$wrapperEl.transition(e), this.emit("setTransition", e, t)
        }, transitionStart: function (e, t) {
            void 0 === e && (e = !0);
            var a = this, i = a.activeIndex, s = a.params, r = a.previousIndex;
            s.autoHeight && a.updateAutoHeight();
            var n = t;
            if (n || (n = r < i ? "next" : i < r ? "prev" : "reset"), a.emit("transitionStart"), e && i !== r) {
                if ("reset" === n) return void a.emit("slideResetTransitionStart");
                a.emit("slideChangeTransitionStart"), "next" === n ? a.emit("slideNextTransitionStart") : a.emit("slidePrevTransitionStart")
            }
        }, transitionEnd: function (e, t) {
            void 0 === e && (e = !0);
            var a = this, i = a.activeIndex, s = a.previousIndex;
            a.animating = !1, a.setTransition(0);
            var r = t;
            if (r || (r = s < i ? "next" : i < s ? "prev" : "reset"), a.emit("transitionEnd"), e && i !== s) {
                if ("reset" === r) return void a.emit("slideResetTransitionEnd");
                a.emit("slideChangeTransitionEnd"), "next" === r ? a.emit("slideNextTransitionEnd") : a.emit("slidePrevTransitionEnd")
            }
        }
    };
    var c = {
        slideTo: function (e, t, a, i) {
            void 0 === e && (e = 0), void 0 === t && (t = this.params.speed), void 0 === a && (a = !0);
            var s = this, r = e;
            r < 0 && (r = 0);
            var n = s.params, o = s.snapGrid, l = s.slidesGrid, d = s.previousIndex, p = s.activeIndex,
                c = s.rtlTranslate;
            if (s.animating && n.preventInteractionOnTransition) return !1;
            var u = Math.floor(r / n.slidesPerGroup);
            u >= o.length && (u = o.length - 1), (p || n.initialSlide || 0) === (d || 0) && a && s.emit("beforeSlideChangeStart");
            var h, v = -o[u];
            if (s.updateProgress(v), n.normalizeSlideIndex) for (var f = 0; f < l.length; f += 1) -Math.floor(100 * v) >= Math.floor(100 * l[f]) && (r = f);
            if (s.initialized && r !== p) {
                if (!s.allowSlideNext && v < s.translate && v < s.minTranslate()) return !1;
                if (!s.allowSlidePrev && v > s.translate && v > s.maxTranslate() && (p || 0) !== r) return !1
            }
            return h = p < r ? "next" : r < p ? "prev" : "reset", c && -v === s.translate || !c && v === s.translate ? (s.updateActiveIndex(r), n.autoHeight && s.updateAutoHeight(), s.updateSlidesClasses(), "slide" !== n.effect && s.setTranslate(v), "reset" !== h && (s.transitionStart(a, h), s.transitionEnd(a, h)), !1) : (0 !== t && R.transition ? (s.setTransition(t), s.setTranslate(v), s.updateActiveIndex(r), s.updateSlidesClasses(), s.emit("beforeTransitionStart", t, i), s.transitionStart(a, h), s.animating || (s.animating = !0, s.onSlideToWrapperTransitionEnd || (s.onSlideToWrapperTransitionEnd = function (e) {
                s && !s.destroyed && e.target === this && (s.$wrapperEl[0].removeEventListener("transitionend", s.onSlideToWrapperTransitionEnd), s.$wrapperEl[0].removeEventListener("webkitTransitionEnd", s.onSlideToWrapperTransitionEnd), s.onSlideToWrapperTransitionEnd = null, delete s.onSlideToWrapperTransitionEnd, s.transitionEnd(a, h))
            }), s.$wrapperEl[0].addEventListener("transitionend", s.onSlideToWrapperTransitionEnd), s.$wrapperEl[0].addEventListener("webkitTransitionEnd", s.onSlideToWrapperTransitionEnd))) : (s.setTransition(0), s.setTranslate(v), s.updateActiveIndex(r), s.updateSlidesClasses(), s.emit("beforeTransitionStart", t, i), s.transitionStart(a, h), s.transitionEnd(a, h)), !0)
        }, slideToLoop: function (e, t, a, i) {
            void 0 === e && (e = 0), void 0 === t && (t = this.params.speed), void 0 === a && (a = !0);
            var s = e;
            return this.params.loop && (s += this.loopedSlides), this.slideTo(s, t, a, i)
        }, slideNext: function (e, t, a) {
            void 0 === e && (e = this.params.speed), void 0 === t && (t = !0);
            var i = this, s = i.params, r = i.animating;
            return s.loop ? !r && (i.loopFix(), i._clientLeft = i.$wrapperEl[0].clientLeft, i.slideTo(i.activeIndex + s.slidesPerGroup, e, t, a)) : i.slideTo(i.activeIndex + s.slidesPerGroup, e, t, a)
        }, slidePrev: function (e, t, a) {
            void 0 === e && (e = this.params.speed), void 0 === t && (t = !0);
            var i = this, s = i.params, r = i.animating, n = i.snapGrid, o = i.slidesGrid, l = i.rtlTranslate;
            if (s.loop) {
                if (r) return !1;
                i.loopFix(), i._clientLeft = i.$wrapperEl[0].clientLeft
            }

            function d(e) {
                return e < 0 ? -Math.floor(Math.abs(e)) : Math.floor(e)
            }

            var p, c = d(l ? i.translate : -i.translate), u = n.map(function (e) {
                return d(e)
            }), h = (o.map(function (e) {
                return d(e)
            }), n[u.indexOf(c)], n[u.indexOf(c) - 1]);
            return void 0 !== h && (p = o.indexOf(h)) < 0 && (p = i.activeIndex - 1), i.slideTo(p, e, t, a)
        }, slideReset: function (e, t, a) {
            return void 0 === e && (e = this.params.speed), void 0 === t && (t = !0), this.slideTo(this.activeIndex, e, t, a)
        }, slideToClosest: function (e, t, a) {
            void 0 === e && (e = this.params.speed), void 0 === t && (t = !0);
            var i = this, s = i.activeIndex, r = Math.floor(s / i.params.slidesPerGroup);
            if (r < i.snapGrid.length - 1) {
                var n = i.rtlTranslate ? i.translate : -i.translate, o = i.snapGrid[r];
                (i.snapGrid[r + 1] - o) / 2 < n - o && (s = i.params.slidesPerGroup)
            }
            return i.slideTo(s, e, t, a)
        }, slideToClickedSlide: function () {
            var e, t = this, a = t.params, i = t.$wrapperEl,
                s = "auto" === a.slidesPerView ? t.slidesPerViewDynamic() : a.slidesPerView, r = t.clickedIndex;
            if (a.loop) {
                if (t.animating) return;
                e = parseInt(L(t.clickedSlide).attr("data-swiper-slide-index"), 10), a.centeredSlides ? r < t.loopedSlides - s / 2 || r > t.slides.length - t.loopedSlides + s / 2 ? (t.loopFix(), r = i.children("." + a.slideClass + '[data-swiper-slide-index="' + e + '"]:not(.' + a.slideDuplicateClass + ")").eq(0).index(), V.nextTick(function () {
                    t.slideTo(r)
                })) : t.slideTo(r) : r > t.slides.length - s ? (t.loopFix(), r = i.children("." + a.slideClass + '[data-swiper-slide-index="' + e + '"]:not(.' + a.slideDuplicateClass + ")").eq(0).index(), V.nextTick(function () {
                    t.slideTo(r)
                })) : t.slideTo(r)
            } else t.slideTo(r)
        }
    };
    var u = {
        loopCreate: function () {
            var i = this, e = i.params, t = i.$wrapperEl;
            t.children("." + e.slideClass + "." + e.slideDuplicateClass).remove();
            var s = t.children("." + e.slideClass);
            if (e.loopFillGroupWithBlank) {
                var a = e.slidesPerGroup - s.length % e.slidesPerGroup;
                if (a !== e.slidesPerGroup) {
                    for (var r = 0; r < a; r += 1) {
                        var n = L(f.createElement("div")).addClass(e.slideClass + " " + e.slideBlankClass);
                        t.append(n)
                    }
                    s = t.children("." + e.slideClass)
                }
            }
            "auto" !== e.slidesPerView || e.loopedSlides || (e.loopedSlides = s.length), i.loopedSlides = parseInt(e.loopedSlides || e.slidesPerView, 10), i.loopedSlides += e.loopAdditionalSlides, i.loopedSlides > s.length && (i.loopedSlides = s.length);
            var o = [], l = [];
            s.each(function (e, t) {
                var a = L(t);
                e < i.loopedSlides && l.push(t), e < s.length && e >= s.length - i.loopedSlides && o.push(t), a.attr("data-swiper-slide-index", e)
            });
            for (var d = 0; d < l.length; d += 1) t.append(L(l[d].cloneNode(!0)).addClass(e.slideDuplicateClass));
            for (var p = o.length - 1; 0 <= p; p -= 1) t.prepend(L(o[p].cloneNode(!0)).addClass(e.slideDuplicateClass))
        }, loopFix: function () {
            var e, t = this, a = t.params, i = t.activeIndex, s = t.slides, r = t.loopedSlides, n = t.allowSlidePrev,
                o = t.allowSlideNext, l = t.snapGrid, d = t.rtlTranslate;
            t.allowSlidePrev = !0, t.allowSlideNext = !0;
            var p = -l[i] - t.getTranslate();
            i < r ? (e = s.length - 3 * r + i, e += r, t.slideTo(e, 0, !1, !0) && 0 !== p && t.setTranslate((d ? -t.translate : t.translate) - p)) : ("auto" === a.slidesPerView && 2 * r <= i || i >= s.length - r) && (e = -s.length + i + r, e += r, t.slideTo(e, 0, !1, !0) && 0 !== p && t.setTranslate((d ? -t.translate : t.translate) - p));
            t.allowSlidePrev = n, t.allowSlideNext = o
        }, loopDestroy: function () {
            var e = this.$wrapperEl, t = this.params, a = this.slides;
            e.children("." + t.slideClass + "." + t.slideDuplicateClass).remove(), a.removeAttr("data-swiper-slide-index")
        }
    };
    var h = {
        setGrabCursor: function (e) {
            if (!(R.touch || !this.params.simulateTouch || this.params.watchOverflow && this.isLocked)) {
                var t = this.el;
                t.style.cursor = "move", t.style.cursor = e ? "-webkit-grabbing" : "-webkit-grab", t.style.cursor = e ? "-moz-grabbin" : "-moz-grab", t.style.cursor = e ? "grabbing" : "grab"
            }
        }, unsetGrabCursor: function () {
            R.touch || this.params.watchOverflow && this.isLocked || (this.el.style.cursor = "")
        }
    };
    var v = {
        appendSlide: function (e) {
            var t = this, a = t.$wrapperEl, i = t.params;
            if (i.loop && t.loopDestroy(), "object" == typeof e && "length" in e) for (var s = 0; s < e.length; s += 1) e[s] && a.append(e[s]); else a.append(e);
            i.loop && t.loopCreate(), i.observer && R.observer || t.update()
        }, prependSlide: function (e) {
            var t = this, a = t.params, i = t.$wrapperEl, s = t.activeIndex;
            a.loop && t.loopDestroy();
            var r = s + 1;
            if ("object" == typeof e && "length" in e) {
                for (var n = 0; n < e.length; n += 1) e[n] && i.prepend(e[n]);
                r = s + e.length
            } else i.prepend(e);
            a.loop && t.loopCreate(), a.observer && R.observer || t.update(), t.slideTo(r, 0, !1)
        }, addSlide: function (e, t) {
            var a = this, i = a.$wrapperEl, s = a.params, r = a.activeIndex;
            s.loop && (r -= a.loopedSlides, a.loopDestroy(), a.slides = i.children("." + s.slideClass));
            var n = a.slides.length;
            if (e <= 0) a.prependSlide(t); else if (n <= e) a.appendSlide(t); else {
                for (var o = e < r ? r + 1 : r, l = [], d = n - 1; e <= d; d -= 1) {
                    var p = a.slides.eq(d);
                    p.remove(), l.unshift(p)
                }
                if ("object" == typeof t && "length" in t) {
                    for (var c = 0; c < t.length; c += 1) t[c] && i.append(t[c]);
                    o = e < r ? r + t.length : r
                } else i.append(t);
                for (var u = 0; u < l.length; u += 1) i.append(l[u]);
                s.loop && a.loopCreate(), s.observer && R.observer || a.update(), s.loop ? a.slideTo(o + a.loopedSlides, 0, !1) : a.slideTo(o, 0, !1)
            }
        }, removeSlide: function (e) {
            var t = this, a = t.params, i = t.$wrapperEl, s = t.activeIndex;
            a.loop && (s -= t.loopedSlides, t.loopDestroy(), t.slides = i.children("." + a.slideClass));
            var r, n = s;
            if ("object" == typeof e && "length" in e) {
                for (var o = 0; o < e.length; o += 1) r = e[o], t.slides[r] && t.slides.eq(r).remove(), r < n && (n -= 1);
                n = Math.max(n, 0)
            } else r = e, t.slides[r] && t.slides.eq(r).remove(), r < n && (n -= 1), n = Math.max(n, 0);
            a.loop && t.loopCreate(), a.observer && R.observer || t.update(), a.loop ? t.slideTo(n + t.loopedSlides, 0, !1) : t.slideTo(n, 0, !1)
        }, removeAllSlides: function () {
            for (var e = [], t = 0; t < this.slides.length; t += 1) e.push(t);
            this.removeSlide(e)
        }
    }, m = function () {
        var e = Y.navigator.userAgent, t = {
                ios: !1,
                android: !1,
                androidChrome: !1,
                desktop: !1,
                windows: !1,
                iphone: !1,
                ipod: !1,
                ipad: !1,
                cordova: Y.cordova || Y.phonegap,
                phonegap: Y.cordova || Y.phonegap
            }, a = e.match(/(Windows Phone);?[\s\/]+([\d.]+)?/), i = e.match(/(Android);?[\s\/]+([\d.]+)?/),
            s = e.match(/(iPad).*OS\s([\d_]+)/), r = e.match(/(iPod)(.*OS\s([\d_]+))?/),
            n = !s && e.match(/(iPhone\sOS|iOS)\s([\d_]+)/);
        if (a && (t.os = "windows", t.osVersion = a[2], t.windows = !0), i && !a && (t.os = "android", t.osVersion = i[2], t.android = !0, t.androidChrome = 0 <= e.toLowerCase().indexOf("chrome")), (s || n || r) && (t.os = "ios", t.ios = !0), n && !r && (t.osVersion = n[2].replace(/_/g, "."), t.iphone = !0), s && (t.osVersion = s[2].replace(/_/g, "."), t.ipad = !0), r && (t.osVersion = r[3] ? r[3].replace(/_/g, ".") : null, t.iphone = !0), t.ios && t.osVersion && 0 <= e.indexOf("Version/") && "10" === t.osVersion.split(".")[0] && (t.osVersion = e.toLowerCase().split("version/")[1].split(" ")[0]), t.desktop = !(t.os || t.android || t.webView), t.webView = (n || s || r) && e.match(/.*AppleWebKit(?!.*Safari)/i), t.os && "ios" === t.os) {
            var o = t.osVersion.split("."), l = f.querySelector('meta[name="viewport"]');
            t.minimalUi = !t.webView && (r || n) && (1 * o[0] == 7 ? 1 <= 1 * o[1] : 7 < 1 * o[0]) && l && 0 <= l.getAttribute("content").indexOf("minimal-ui")
        }
        return t.pixelRatio = Y.devicePixelRatio || 1, t
    }();

    function g() {
        var e = this, t = e.params, a = e.el;
        if (!a || 0 !== a.offsetWidth) {
            t.breakpoints && e.setBreakpoint();
            var i = e.allowSlideNext, s = e.allowSlidePrev, r = e.snapGrid;
            if (e.allowSlideNext = !0, e.allowSlidePrev = !0, e.updateSize(), e.updateSlides(), t.freeMode) {
                var n = Math.min(Math.max(e.translate, e.maxTranslate()), e.minTranslate());
                e.setTranslate(n), e.updateActiveIndex(), e.updateSlidesClasses(), t.autoHeight && e.updateAutoHeight()
            } else e.updateSlidesClasses(), ("auto" === t.slidesPerView || 1 < t.slidesPerView) && e.isEnd && !e.params.centeredSlides ? e.slideTo(e.slides.length - 1, 0, !1, !0) : e.slideTo(e.activeIndex, 0, !1, !0);
            e.allowSlidePrev = s, e.allowSlideNext = i, e.params.watchOverflow && r !== e.snapGrid && e.checkOverflow()
        }
    }

    var b = {
        attachEvents: function () {
            var e = this, t = e.params, a = e.touchEvents, i = e.el, s = e.wrapperEl;
            e.onTouchStart = function (e) {
                var t = this, a = t.touchEventsData, i = t.params, s = t.touches;
                if (!t.animating || !i.preventInteractionOnTransition) {
                    var r = e;
                    if (r.originalEvent && (r = r.originalEvent), a.isTouchEvent = "touchstart" === r.type, (a.isTouchEvent || !("which" in r) || 3 !== r.which) && !(!a.isTouchEvent && "button" in r && 0 < r.button || a.isTouched && a.isMoved)) if (i.noSwiping && L(r.target).closest(i.noSwipingSelector ? i.noSwipingSelector : "." + i.noSwipingClass)[0]) t.allowClick = !0; else if (!i.swipeHandler || L(r).closest(i.swipeHandler)[0]) {
                        s.currentX = "touchstart" === r.type ? r.targetTouches[0].pageX : r.pageX, s.currentY = "touchstart" === r.type ? r.targetTouches[0].pageY : r.pageY;
                        var n = s.currentX, o = s.currentY, l = i.edgeSwipeDetection || i.iOSEdgeSwipeDetection,
                            d = i.edgeSwipeThreshold || i.iOSEdgeSwipeThreshold;
                        if (!l || !(n <= d || n >= Y.screen.width - d)) {
                            if (V.extend(a, {
                                isTouched: !0,
                                isMoved: !1,
                                allowTouchCallbacks: !0,
                                isScrolling: void 0,
                                startMoving: void 0
                            }), s.startX = n, s.startY = o, a.touchStartTime = V.now(), t.allowClick = !0, t.updateSize(), t.swipeDirection = void 0, 0 < i.threshold && (a.allowThresholdMove = !1), "touchstart" !== r.type) {
                                var p = !0;
                                L(r.target).is(a.formElements) && (p = !1), f.activeElement && L(f.activeElement).is(a.formElements) && f.activeElement !== r.target && f.activeElement.blur(), p && t.allowTouchMove && i.touchStartPreventDefault && r.preventDefault()
                            }
                            t.emit("touchStart", r)
                        }
                    }
                }
            }.bind(e), e.onTouchMove = function (e) {
                var t = this, a = t.touchEventsData, i = t.params, s = t.touches, r = t.rtlTranslate, n = e;
                if (n.originalEvent && (n = n.originalEvent), a.isTouched) {
                    if (!a.isTouchEvent || "mousemove" !== n.type) {
                        var o = "touchmove" === n.type ? n.targetTouches[0].pageX : n.pageX,
                            l = "touchmove" === n.type ? n.targetTouches[0].pageY : n.pageY;
                        if (n.preventedByNestedSwiper) return s.startX = o, void (s.startY = l);
                        if (!t.allowTouchMove) return t.allowClick = !1, void (a.isTouched && (V.extend(s, {
                            startX: o,
                            startY: l,
                            currentX: o,
                            currentY: l
                        }), a.touchStartTime = V.now()));
                        if (a.isTouchEvent && i.touchReleaseOnEdges && !i.loop) if (t.isVertical()) {
                            if (l < s.startY && t.translate <= t.maxTranslate() || l > s.startY && t.translate >= t.minTranslate()) return a.isTouched = !1, void (a.isMoved = !1)
                        } else if (o < s.startX && t.translate <= t.maxTranslate() || o > s.startX && t.translate >= t.minTranslate()) return;
                        if (a.isTouchEvent && f.activeElement && n.target === f.activeElement && L(n.target).is(a.formElements)) return a.isMoved = !0, void (t.allowClick = !1);
                        if (a.allowTouchCallbacks && t.emit("touchMove", n), !(n.targetTouches && 1 < n.targetTouches.length)) {
                            s.currentX = o, s.currentY = l;
                            var d, p = s.currentX - s.startX, c = s.currentY - s.startY;
                            if (!(t.params.threshold && Math.sqrt(Math.pow(p, 2) + Math.pow(c, 2)) < t.params.threshold)) if (void 0 === a.isScrolling && (t.isHorizontal() && s.currentY === s.startY || t.isVertical() && s.currentX === s.startX ? a.isScrolling = !1 : 25 <= p * p + c * c && (d = 180 * Math.atan2(Math.abs(c), Math.abs(p)) / Math.PI, a.isScrolling = t.isHorizontal() ? d > i.touchAngle : 90 - d > i.touchAngle)), a.isScrolling && t.emit("touchMoveOpposite", n), void 0 === a.startMoving && (s.currentX === s.startX && s.currentY === s.startY || (a.startMoving = !0)), a.isScrolling) a.isTouched = !1; else if (a.startMoving) {
                                t.allowClick = !1, n.preventDefault(), i.touchMoveStopPropagation && !i.nested && n.stopPropagation(), a.isMoved || (i.loop && t.loopFix(), a.startTranslate = t.getTranslate(), t.setTransition(0), t.animating && t.$wrapperEl.trigger("webkitTransitionEnd transitionend"), a.allowMomentumBounce = !1, !i.grabCursor || !0 !== t.allowSlideNext && !0 !== t.allowSlidePrev || t.setGrabCursor(!0), t.emit("sliderFirstMove", n)), t.emit("sliderMove", n), a.isMoved = !0;
                                var u = t.isHorizontal() ? p : c;
                                s.diff = u, u *= i.touchRatio, r && (u = -u), t.swipeDirection = 0 < u ? "prev" : "next", a.currentTranslate = u + a.startTranslate;
                                var h = !0, v = i.resistanceRatio;
                                if (i.touchReleaseOnEdges && (v = 0), 0 < u && a.currentTranslate > t.minTranslate() ? (h = !1, i.resistance && (a.currentTranslate = t.minTranslate() - 1 + Math.pow(-t.minTranslate() + a.startTranslate + u, v))) : u < 0 && a.currentTranslate < t.maxTranslate() && (h = !1, i.resistance && (a.currentTranslate = t.maxTranslate() + 1 - Math.pow(t.maxTranslate() - a.startTranslate - u, v))), h && (n.preventedByNestedSwiper = !0), !t.allowSlideNext && "next" === t.swipeDirection && a.currentTranslate < a.startTranslate && (a.currentTranslate = a.startTranslate), !t.allowSlidePrev && "prev" === t.swipeDirection && a.currentTranslate > a.startTranslate && (a.currentTranslate = a.startTranslate), 0 < i.threshold) {
                                    if (!(Math.abs(u) > i.threshold || a.allowThresholdMove)) return void (a.currentTranslate = a.startTranslate);
                                    if (!a.allowThresholdMove) return a.allowThresholdMove = !0, s.startX = s.currentX, s.startY = s.currentY, a.currentTranslate = a.startTranslate, void (s.diff = t.isHorizontal() ? s.currentX - s.startX : s.currentY - s.startY)
                                }
                                i.followFinger && ((i.freeMode || i.watchSlidesProgress || i.watchSlidesVisibility) && (t.updateActiveIndex(), t.updateSlidesClasses()), i.freeMode && (0 === a.velocities.length && a.velocities.push({
                                    position: s[t.isHorizontal() ? "startX" : "startY"],
                                    time: a.touchStartTime
                                }), a.velocities.push({
                                    position: s[t.isHorizontal() ? "currentX" : "currentY"],
                                    time: V.now()
                                })), t.updateProgress(a.currentTranslate), t.setTranslate(a.currentTranslate))
                            }
                        }
                    }
                } else a.startMoving && a.isScrolling && t.emit("touchMoveOpposite", n)
            }.bind(e), e.onTouchEnd = function (e) {
                var t = this, a = t.touchEventsData, i = t.params, s = t.touches, r = t.rtlTranslate, n = t.$wrapperEl,
                    o = t.slidesGrid, l = t.snapGrid, d = e;
                if (d.originalEvent && (d = d.originalEvent), a.allowTouchCallbacks && t.emit("touchEnd", d), a.allowTouchCallbacks = !1, !a.isTouched) return a.isMoved && i.grabCursor && t.setGrabCursor(!1), a.isMoved = !1, void (a.startMoving = !1);
                i.grabCursor && a.isMoved && a.isTouched && (!0 === t.allowSlideNext || !0 === t.allowSlidePrev) && t.setGrabCursor(!1);
                var p, c = V.now(), u = c - a.touchStartTime;
                if (t.allowClick && (t.updateClickedSlide(d), t.emit("tap", d), u < 300 && 300 < c - a.lastClickTime && (a.clickTimeout && clearTimeout(a.clickTimeout), a.clickTimeout = V.nextTick(function () {
                    t && !t.destroyed && t.emit("click", d)
                }, 300)), u < 300 && c - a.lastClickTime < 300 && (a.clickTimeout && clearTimeout(a.clickTimeout), t.emit("doubleTap", d))), a.lastClickTime = V.now(), V.nextTick(function () {
                    t.destroyed || (t.allowClick = !0)
                }), !a.isTouched || !a.isMoved || !t.swipeDirection || 0 === s.diff || a.currentTranslate === a.startTranslate) return a.isTouched = !1, a.isMoved = !1, void (a.startMoving = !1);
                if (a.isTouched = !1, a.isMoved = !1, a.startMoving = !1, p = i.followFinger ? r ? t.translate : -t.translate : -a.currentTranslate, i.freeMode) {
                    if (p < -t.minTranslate()) return void t.slideTo(t.activeIndex);
                    if (p > -t.maxTranslate()) return void (t.slides.length < l.length ? t.slideTo(l.length - 1) : t.slideTo(t.slides.length - 1));
                    if (i.freeModeMomentum) {
                        if (1 < a.velocities.length) {
                            var h = a.velocities.pop(), v = a.velocities.pop(), f = h.position - v.position,
                                m = h.time - v.time;
                            t.velocity = f / m, t.velocity /= 2, Math.abs(t.velocity) < i.freeModeMinimumVelocity && (t.velocity = 0), (150 < m || 300 < V.now() - h.time) && (t.velocity = 0)
                        } else t.velocity = 0;
                        t.velocity *= i.freeModeMomentumVelocityRatio, a.velocities.length = 0;
                        var g = 1e3 * i.freeModeMomentumRatio, b = t.velocity * g, w = t.translate + b;
                        r && (w = -w);
                        var y, x, T = !1, E = 20 * Math.abs(t.velocity) * i.freeModeMomentumBounceRatio;
                        if (w < t.maxTranslate()) i.freeModeMomentumBounce ? (w + t.maxTranslate() < -E && (w = t.maxTranslate() - E), y = t.maxTranslate(), T = !0, a.allowMomentumBounce = !0) : w = t.maxTranslate(), i.loop && i.centeredSlides && (x = !0); else if (w > t.minTranslate()) i.freeModeMomentumBounce ? (w - t.minTranslate() > E && (w = t.minTranslate() + E), y = t.minTranslate(), T = !0, a.allowMomentumBounce = !0) : w = t.minTranslate(), i.loop && i.centeredSlides && (x = !0); else if (i.freeModeSticky) {
                            for (var S, C = 0; C < l.length; C += 1) if (l[C] > -w) {
                                S = C;
                                break
                            }
                            w = -(w = Math.abs(l[S] - w) < Math.abs(l[S - 1] - w) || "next" === t.swipeDirection ? l[S] : l[S - 1])
                        }
                        if (x && t.once("transitionEnd", function () {
                            t.loopFix()
                        }), 0 !== t.velocity) g = r ? Math.abs((-w - t.translate) / t.velocity) : Math.abs((w - t.translate) / t.velocity); else if (i.freeModeSticky) return void t.slideToClosest();
                        i.freeModeMomentumBounce && T ? (t.updateProgress(y), t.setTransition(g), t.setTranslate(w), t.transitionStart(!0, t.swipeDirection), t.animating = !0, n.transitionEnd(function () {
                            t && !t.destroyed && a.allowMomentumBounce && (t.emit("momentumBounce"), t.setTransition(i.speed), t.setTranslate(y), n.transitionEnd(function () {
                                t && !t.destroyed && t.transitionEnd()
                            }))
                        })) : t.velocity ? (t.updateProgress(w), t.setTransition(g), t.setTranslate(w), t.transitionStart(!0, t.swipeDirection), t.animating || (t.animating = !0, n.transitionEnd(function () {
                            t && !t.destroyed && t.transitionEnd()
                        }))) : t.updateProgress(w), t.updateActiveIndex(), t.updateSlidesClasses()
                    } else if (i.freeModeSticky) return void t.slideToClosest();
                    (!i.freeModeMomentum || u >= i.longSwipesMs) && (t.updateProgress(), t.updateActiveIndex(), t.updateSlidesClasses())
                } else {
                    for (var M = 0, k = t.slidesSizesGrid[0], z = 0; z < o.length; z += i.slidesPerGroup) void 0 !== o[z + i.slidesPerGroup] ? p >= o[z] && p < o[z + i.slidesPerGroup] && (k = o[(M = z) + i.slidesPerGroup] - o[z]) : p >= o[z] && (M = z, k = o[o.length - 1] - o[o.length - 2]);
                    var P = (p - o[M]) / k;
                    if (u > i.longSwipesMs) {
                        if (!i.longSwipes) return void t.slideTo(t.activeIndex);
                        "next" === t.swipeDirection && (P >= i.longSwipesRatio ? t.slideTo(M + i.slidesPerGroup) : t.slideTo(M)), "prev" === t.swipeDirection && (P > 1 - i.longSwipesRatio ? t.slideTo(M + i.slidesPerGroup) : t.slideTo(M))
                    } else {
                        if (!i.shortSwipes) return void t.slideTo(t.activeIndex);
                        "next" === t.swipeDirection && t.slideTo(M + i.slidesPerGroup), "prev" === t.swipeDirection && t.slideTo(M)
                    }
                }
            }.bind(e), e.onClick = function (e) {
                this.allowClick || (this.params.preventClicks && e.preventDefault(), this.params.preventClicksPropagation && this.animating && (e.stopPropagation(), e.stopImmediatePropagation()))
            }.bind(e);
            var r = "container" === t.touchEventsTarget ? i : s, n = !!t.nested;
            if (R.touch || !R.pointerEvents && !R.prefixedPointerEvents) {
                if (R.touch) {
                    var o = !("touchstart" !== a.start || !R.passiveListener || !t.passiveListeners) && {
                        passive: !0,
                        capture: !1
                    };
                    r.addEventListener(a.start, e.onTouchStart, o), r.addEventListener(a.move, e.onTouchMove, R.passiveListener ? {
                        passive: !1,
                        capture: n
                    } : n), r.addEventListener(a.end, e.onTouchEnd, o)
                }
                (t.simulateTouch && !m.ios && !m.android || t.simulateTouch && !R.touch && m.ios) && (r.addEventListener("mousedown", e.onTouchStart, !1), f.addEventListener("mousemove", e.onTouchMove, n), f.addEventListener("mouseup", e.onTouchEnd, !1))
            } else r.addEventListener(a.start, e.onTouchStart, !1), f.addEventListener(a.move, e.onTouchMove, n), f.addEventListener(a.end, e.onTouchEnd, !1);
            (t.preventClicks || t.preventClicksPropagation) && r.addEventListener("click", e.onClick, !0), e.on(m.ios || m.android ? "resize orientationchange observerUpdate" : "resize observerUpdate", g, !0)
        }, detachEvents: function () {
            var e = this, t = e.params, a = e.touchEvents, i = e.el, s = e.wrapperEl,
                r = "container" === t.touchEventsTarget ? i : s, n = !!t.nested;
            if (R.touch || !R.pointerEvents && !R.prefixedPointerEvents) {
                if (R.touch) {
                    var o = !("onTouchStart" !== a.start || !R.passiveListener || !t.passiveListeners) && {
                        passive: !0,
                        capture: !1
                    };
                    r.removeEventListener(a.start, e.onTouchStart, o), r.removeEventListener(a.move, e.onTouchMove, n), r.removeEventListener(a.end, e.onTouchEnd, o)
                }
                (t.simulateTouch && !m.ios && !m.android || t.simulateTouch && !R.touch && m.ios) && (r.removeEventListener("mousedown", e.onTouchStart, !1), f.removeEventListener("mousemove", e.onTouchMove, n), f.removeEventListener("mouseup", e.onTouchEnd, !1))
            } else r.removeEventListener(a.start, e.onTouchStart, !1), f.removeEventListener(a.move, e.onTouchMove, n), f.removeEventListener(a.end, e.onTouchEnd, !1);
            (t.preventClicks || t.preventClicksPropagation) && r.removeEventListener("click", e.onClick, !0), e.off(m.ios || m.android ? "resize orientationchange observerUpdate" : "resize observerUpdate", g)
        }
    };
    var w, y = {
        setBreakpoint: function () {
            var e = this, t = e.activeIndex, a = e.initialized, i = e.loopedSlides;
            void 0 === i && (i = 0);
            var s = e.params, r = s.breakpoints;
            if (r && (!r || 0 !== Object.keys(r).length)) {
                var n = e.getBreakpoint(r);
                if (n && e.currentBreakpoint !== n) {
                    var o = n in r ? r[n] : e.originalParams, l = s.loop && o.slidesPerView !== s.slidesPerView;
                    V.extend(e.params, o), V.extend(e, {
                        allowTouchMove: e.params.allowTouchMove,
                        allowSlideNext: e.params.allowSlideNext,
                        allowSlidePrev: e.params.allowSlidePrev
                    }), e.currentBreakpoint = n, l && a && (e.loopDestroy(), e.loopCreate(), e.updateSlides(), e.slideTo(t - i + e.loopedSlides, 0, !1)), e.emit("breakpoint", o)
                }
            }
        }, getBreakpoint: function (e) {
            if (e) {
                var t = !1, a = [];
                Object.keys(e).forEach(function (e) {
                    a.push(e)
                }), a.sort(function (e, t) {
                    return parseInt(e, 10) - parseInt(t, 10)
                });
                for (var i = 0; i < a.length; i += 1) {
                    var s = a[i];
                    this.params.breakpointsInverse ? s <= Y.innerWidth && (t = s) : s >= Y.innerWidth && !t && (t = s)
                }
                return t || "max"
            }
        }
    }, I = {
        isIE: !!Y.navigator.userAgent.match(/Trident/g) || !!Y.navigator.userAgent.match(/MSIE/g),
        isEdge: !!Y.navigator.userAgent.match(/Edge/g),
        isSafari: (w = Y.navigator.userAgent.toLowerCase(), 0 <= w.indexOf("safari") && w.indexOf("chrome") < 0 && w.indexOf("android") < 0),
        isUiWebView: /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(Y.navigator.userAgent)
    };
    var x = {
            init: !0,
            direction: "horizontal",
            touchEventsTarget: "container",
            initialSlide: 0,
            speed: 300,
            preventInteractionOnTransition: !1,
            edgeSwipeDetection: !1,
            edgeSwipeThreshold: 20,
            freeMode: !1,
            freeModeMomentum: !0,
            freeModeMomentumRatio: 1,
            freeModeMomentumBounce: !0,
            freeModeMomentumBounceRatio: 1,
            freeModeMomentumVelocityRatio: 1,
            freeModeSticky: !1,
            freeModeMinimumVelocity: .02,
            autoHeight: !1,
            setWrapperSize: !1,
            virtualTranslate: !1,
            effect: "slide",
            breakpoints: void 0,
            breakpointsInverse: !1,
            spaceBetween: 0,
            slidesPerView: 1,
            slidesPerColumn: 1,
            slidesPerColumnFill: "column",
            slidesPerGroup: 1,
            centeredSlides: !1,
            slidesOffsetBefore: 0,
            slidesOffsetAfter: 0,
            normalizeSlideIndex: !0,
            centerInsufficientSlides: !1,
            watchOverflow: !1,
            roundLengths: !1,
            touchRatio: 1,
            touchAngle: 45,
            simulateTouch: !0,
            shortSwipes: !0,
            longSwipes: !0,
            longSwipesRatio: .5,
            longSwipesMs: 300,
            followFinger: !0,
            allowTouchMove: !0,
            threshold: 0,
            touchMoveStopPropagation: !0,
            touchStartPreventDefault: !0,
            touchReleaseOnEdges: !1,
            uniqueNavElements: !0,
            resistance: !0,
            resistanceRatio: .85,
            watchSlidesProgress: !1,
            watchSlidesVisibility: !1,
            grabCursor: !1,
            preventClicks: !0,
            preventClicksPropagation: !0,
            slideToClickedSlide: !1,
            preloadImages: !0,
            updateOnImagesReady: !0,
            loop: !1,
            loopAdditionalSlides: 0,
            loopedSlides: null,
            loopFillGroupWithBlank: !1,
            allowSlidePrev: !0,
            allowSlideNext: !0,
            swipeHandler: null,
            noSwiping: !0,
            noSwipingClass: "swiper-no-swiping",
            noSwipingSelector: null,
            passiveListeners: !0,
            containerModifierClass: "swiper-container-",
            slideClass: "swiper-slide",
            slideBlankClass: "swiper-slide-invisible-blank",
            slideActiveClass: "swiper-slide-active",
            slideDuplicateActiveClass: "swiper-slide-duplicate-active",
            slideVisibleClass: "swiper-slide-visible",
            slideDuplicateClass: "swiper-slide-duplicate",
            slideNextClass: "swiper-slide-next",
            slideDuplicateNextClass: "swiper-slide-duplicate-next",
            slidePrevClass: "swiper-slide-prev",
            slideDuplicatePrevClass: "swiper-slide-duplicate-prev",
            wrapperClass: "swiper-wrapper",
            runCallbacksOnInit: !0
        }, T = {
            update: o,
            translate: d,
            transition: p,
            slide: c,
            loop: u,
            grabCursor: h,
            manipulation: v,
            events: b,
            breakpoints: y,
            checkOverflow: {
                checkOverflow: function () {
                    var e = this, t = e.isLocked;
                    e.isLocked = 1 === e.snapGrid.length, e.allowSlideNext = !e.isLocked, e.allowSlidePrev = !e.isLocked, t !== e.isLocked && e.emit(e.isLocked ? "lock" : "unlock"), t && t !== e.isLocked && (e.isEnd = !1, e.navigation.update())
                }
            },
            classes: {
                addClasses: function () {
                    var t = this.classNames, a = this.params, e = this.rtl, i = this.$el, s = [];
                    s.push(a.direction), a.freeMode && s.push("free-mode"), R.flexbox || s.push("no-flexbox"), a.autoHeight && s.push("autoheight"), e && s.push("rtl"), 1 < a.slidesPerColumn && s.push("multirow"), m.android && s.push("android"), m.ios && s.push("ios"), (I.isIE || I.isEdge) && (R.pointerEvents || R.prefixedPointerEvents) && s.push("wp8-" + a.direction), s.forEach(function (e) {
                        t.push(a.containerModifierClass + e)
                    }), i.addClass(t.join(" "))
                }, removeClasses: function () {
                    var e = this.$el, t = this.classNames;
                    e.removeClass(t.join(" "))
                }
            },
            images: {
                loadImage: function (e, t, a, i, s, r) {
                    var n;

                    function o() {
                        r && r()
                    }

                    e.complete && s ? o() : t ? ((n = new Y.Image).onload = o, n.onerror = o, i && (n.sizes = i), a && (n.srcset = a), t && (n.src = t)) : o()
                }, preloadImages: function () {
                    var e = this;

                    function t() {
                        null != e && e && !e.destroyed && (void 0 !== e.imagesLoaded && (e.imagesLoaded += 1), e.imagesLoaded === e.imagesToLoad.length && (e.params.updateOnImagesReady && e.update(), e.emit("imagesReady")))
                    }

                    e.imagesToLoad = e.$el.find("img");
                    for (var a = 0; a < e.imagesToLoad.length; a += 1) {
                        var i = e.imagesToLoad[a];
                        e.loadImage(i, i.currentSrc || i.getAttribute("src"), i.srcset || i.getAttribute("srcset"), i.sizes || i.getAttribute("sizes"), !0, t)
                    }
                }
            }
        }, E = {}, S = function (u) {
            function h() {
                for (var e, t, s, a = [], i = arguments.length; i--;) a[i] = arguments[i];
                1 === a.length && a[0].constructor && a[0].constructor === Object ? s = a[0] : (t = (e = a)[0], s = e[1]), s || (s = {}), s = V.extend({}, s), t && !s.el && (s.el = t), u.call(this, s), Object.keys(T).forEach(function (t) {
                    Object.keys(T[t]).forEach(function (e) {
                        h.prototype[e] || (h.prototype[e] = T[t][e])
                    })
                });
                var r = this;
                void 0 === r.modules && (r.modules = {}), Object.keys(r.modules).forEach(function (e) {
                    var t = r.modules[e];
                    if (t.params) {
                        var a = Object.keys(t.params)[0], i = t.params[a];
                        if ("object" != typeof i || null === i) return;
                        if (!(a in s && "enabled" in i)) return;
                        !0 === s[a] && (s[a] = {enabled: !0}), "object" != typeof s[a] || "enabled" in s[a] || (s[a].enabled = !0), s[a] || (s[a] = {enabled: !1})
                    }
                });
                var n = V.extend({}, x);
                r.useModulesParams(n), r.params = V.extend({}, n, E, s), r.originalParams = V.extend({}, r.params), r.passedParams = V.extend({}, s);
                var o = (r.$ = L)(r.params.el);
                if (t = o[0]) {
                    if (1 < o.length) {
                        var l = [];
                        return o.each(function (e, t) {
                            var a = V.extend({}, s, {el: t});
                            l.push(new h(a))
                        }), l
                    }
                    t.swiper = r, o.data("swiper", r);
                    var d, p, c = o.children("." + r.params.wrapperClass);
                    return V.extend(r, {
                        $el: o,
                        el: t,
                        $wrapperEl: c,
                        wrapperEl: c[0],
                        classNames: [],
                        slides: L(),
                        slidesGrid: [],
                        snapGrid: [],
                        slidesSizesGrid: [],
                        isHorizontal: function () {
                            return "horizontal" === r.params.direction
                        },
                        isVertical: function () {
                            return "vertical" === r.params.direction
                        },
                        rtl: "rtl" === t.dir.toLowerCase() || "rtl" === o.css("direction"),
                        rtlTranslate: "horizontal" === r.params.direction && ("rtl" === t.dir.toLowerCase() || "rtl" === o.css("direction")),
                        wrongRTL: "-webkit-box" === c.css("display"),
                        activeIndex: 0,
                        realIndex: 0,
                        isBeginning: !0,
                        isEnd: !1,
                        translate: 0,
                        previousTranslate: 0,
                        progress: 0,
                        velocity: 0,
                        animating: !1,
                        allowSlideNext: r.params.allowSlideNext,
                        allowSlidePrev: r.params.allowSlidePrev,
                        touchEvents: (d = ["touchstart", "touchmove", "touchend"], p = ["mousedown", "mousemove", "mouseup"], R.pointerEvents ? p = ["pointerdown", "pointermove", "pointerup"] : R.prefixedPointerEvents && (p = ["MSPointerDown", "MSPointerMove", "MSPointerUp"]), r.touchEventsTouch = {
                            start: d[0],
                            move: d[1],
                            end: d[2]
                        }, r.touchEventsDesktop = {
                            start: p[0],
                            move: p[1],
                            end: p[2]
                        }, R.touch || !r.params.simulateTouch ? r.touchEventsTouch : r.touchEventsDesktop),
                        touchEventsData: {
                            isTouched: void 0,
                            isMoved: void 0,
                            allowTouchCallbacks: void 0,
                            touchStartTime: void 0,
                            isScrolling: void 0,
                            currentTranslate: void 0,
                            startTranslate: void 0,
                            allowThresholdMove: void 0,
                            formElements: "input, select, option, textarea, button, video",
                            lastClickTime: V.now(),
                            clickTimeout: void 0,
                            velocities: [],
                            allowMomentumBounce: void 0,
                            isTouchEvent: void 0,
                            startMoving: void 0
                        },
                        allowClick: !0,
                        allowTouchMove: r.params.allowTouchMove,
                        touches: {startX: 0, startY: 0, currentX: 0, currentY: 0, diff: 0},
                        imagesToLoad: [],
                        imagesLoaded: 0
                    }), r.useModules(), r.params.init && r.init(), r
                }
            }

            u && (h.__proto__ = u);
            var e = {
                extendedDefaults: {configurable: !0},
                defaults: {configurable: !0},
                Class: {configurable: !0},
                $: {configurable: !0}
            };
            return ((h.prototype = Object.create(u && u.prototype)).constructor = h).prototype.slidesPerViewDynamic = function () {
                var e = this, t = e.params, a = e.slides, i = e.slidesGrid, s = e.size, r = e.activeIndex, n = 1;
                if (t.centeredSlides) {
                    for (var o, l = a[r].swiperSlideSize, d = r + 1; d < a.length; d += 1) a[d] && !o && (n += 1, s < (l += a[d].swiperSlideSize) && (o = !0));
                    for (var p = r - 1; 0 <= p; p -= 1) a[p] && !o && (n += 1, s < (l += a[p].swiperSlideSize) && (o = !0))
                } else for (var c = r + 1; c < a.length; c += 1) i[c] - i[r] < s && (n += 1);
                return n
            }, h.prototype.update = function () {
                var a = this;
                if (a && !a.destroyed) {
                    var e = a.snapGrid, t = a.params;
                    t.breakpoints && a.setBreakpoint(), a.updateSize(), a.updateSlides(), a.updateProgress(), a.updateSlidesClasses(), a.params.freeMode ? (i(), a.params.autoHeight && a.updateAutoHeight()) : (("auto" === a.params.slidesPerView || 1 < a.params.slidesPerView) && a.isEnd && !a.params.centeredSlides ? a.slideTo(a.slides.length - 1, 0, !1, !0) : a.slideTo(a.activeIndex, 0, !1, !0)) || i(), t.watchOverflow && e !== a.snapGrid && a.checkOverflow(), a.emit("update")
                }

                function i() {
                    var e = a.rtlTranslate ? -1 * a.translate : a.translate,
                        t = Math.min(Math.max(e, a.maxTranslate()), a.minTranslate());
                    a.setTranslate(t), a.updateActiveIndex(), a.updateSlidesClasses()
                }
            }, h.prototype.init = function () {
                var e = this;
                e.initialized || (e.emit("beforeInit"), e.params.breakpoints && e.setBreakpoint(), e.addClasses(), e.params.loop && e.loopCreate(), e.updateSize(), e.updateSlides(), e.params.watchOverflow && e.checkOverflow(), e.params.grabCursor && e.setGrabCursor(), e.params.preloadImages && e.preloadImages(), e.params.loop ? e.slideTo(e.params.initialSlide + e.loopedSlides, 0, e.params.runCallbacksOnInit) : e.slideTo(e.params.initialSlide, 0, e.params.runCallbacksOnInit), e.attachEvents(), e.initialized = !0, e.emit("init"))
            }, h.prototype.destroy = function (e, t) {
                void 0 === e && (e = !0), void 0 === t && (t = !0);
                var a = this, i = a.params, s = a.$el, r = a.$wrapperEl, n = a.slides;
                return void 0 === a.params || a.destroyed || (a.emit("beforeDestroy"), a.initialized = !1, a.detachEvents(), i.loop && a.loopDestroy(), t && (a.removeClasses(), s.removeAttr("style"), r.removeAttr("style"), n && n.length && n.removeClass([i.slideVisibleClass, i.slideActiveClass, i.slideNextClass, i.slidePrevClass].join(" ")).removeAttr("style").removeAttr("data-swiper-slide-index").removeAttr("data-swiper-column").removeAttr("data-swiper-row")), a.emit("destroy"), Object.keys(a.eventsListeners).forEach(function (e) {
                    a.off(e)
                }), !1 !== e && (a.$el[0].swiper = null, a.$el.data("swiper", null), V.deleteProps(a)), a.destroyed = !0), null
            }, h.extendDefaults = function (e) {
                V.extend(E, e)
            }, e.extendedDefaults.get = function () {
                return E
            }, e.defaults.get = function () {
                return x
            }, e.Class.get = function () {
                return u
            }, e.$.get = function () {
                return L
            }, Object.defineProperties(h, e), h
        }(s), C = {name: "device", proto: {device: m}, static: {device: m}},
        M = {name: "support", proto: {support: R}, static: {support: R}},
        k = {name: "browser", proto: {browser: I}, static: {browser: I}}, z = {
            name: "resize", create: function () {
                var e = this;
                V.extend(e, {
                    resize: {
                        resizeHandler: function () {
                            e && !e.destroyed && e.initialized && (e.emit("beforeResize"), e.emit("resize"))
                        }, orientationChangeHandler: function () {
                            e && !e.destroyed && e.initialized && e.emit("orientationchange")
                        }
                    }
                })
            }, on: {
                init: function () {
                    Y.addEventListener("resize", this.resize.resizeHandler), Y.addEventListener("orientationchange", this.resize.orientationChangeHandler)
                }, destroy: function () {
                    Y.removeEventListener("resize", this.resize.resizeHandler), Y.removeEventListener("orientationchange", this.resize.orientationChangeHandler)
                }
            }
        }, P = {
            func: Y.MutationObserver || Y.WebkitMutationObserver, attach: function (e, t) {
                void 0 === t && (t = {});
                var a = this, i = new P.func(function (e) {
                    if (1 !== e.length) {
                        var t = function () {
                            a.emit("observerUpdate", e[0])
                        };
                        Y.requestAnimationFrame ? Y.requestAnimationFrame(t) : Y.setTimeout(t, 0)
                    } else a.emit("observerUpdate", e[0])
                });
                i.observe(e, {
                    attributes: void 0 === t.attributes || t.attributes,
                    childList: void 0 === t.childList || t.childList,
                    characterData: void 0 === t.characterData || t.characterData
                }), a.observer.observers.push(i)
            }, init: function () {
                var e = this;
                if (R.observer && e.params.observer) {
                    if (e.params.observeParents) for (var t = e.$el.parents(), a = 0; a < t.length; a += 1) e.observer.attach(t[a]);
                    e.observer.attach(e.$el[0], {childList: !1}), e.observer.attach(e.$wrapperEl[0], {attributes: !1})
                }
            }, destroy: function () {
                this.observer.observers.forEach(function (e) {
                    e.disconnect()
                }), this.observer.observers = []
            }
        }, $ = {
            name: "observer", params: {observer: !1, observeParents: !1}, create: function () {
                V.extend(this, {
                    observer: {
                        init: P.init.bind(this),
                        attach: P.attach.bind(this),
                        destroy: P.destroy.bind(this),
                        observers: []
                    }
                })
            }, on: {
                init: function () {
                    this.observer.init()
                }, destroy: function () {
                    this.observer.destroy()
                }
            }
        }, D = {
            update: function (e) {
                var t = this, a = t.params, i = a.slidesPerView, s = a.slidesPerGroup, r = a.centeredSlides,
                    n = t.params.virtual, o = n.addSlidesBefore, l = n.addSlidesAfter, d = t.virtual, p = d.from, c = d.to,
                    u = d.slides, h = d.slidesGrid, v = d.renderSlide, f = d.offset;
                t.updateActiveIndex();
                var m, g, b, w = t.activeIndex || 0;
                m = t.rtlTranslate ? "right" : t.isHorizontal() ? "left" : "top", r ? (g = Math.floor(i / 2) + s + o, b = Math.floor(i / 2) + s + l) : (g = i + (s - 1) + o, b = s + l);
                var y = Math.max((w || 0) - b, 0), x = Math.min((w || 0) + g, u.length - 1),
                    T = (t.slidesGrid[y] || 0) - (t.slidesGrid[0] || 0);

                function E() {
                    t.updateSlides(), t.updateProgress(), t.updateSlidesClasses(), t.lazy && t.params.lazy.enabled && t.lazy.load()
                }

                if (V.extend(t.virtual, {
                    from: y,
                    to: x,
                    offset: T,
                    slidesGrid: t.slidesGrid
                }), p === y && c === x && !e) return t.slidesGrid !== h && T !== f && t.slides.css(m, T + "px"), void t.updateProgress();
                if (t.params.virtual.renderExternal) return t.params.virtual.renderExternal.call(t, {
                    offset: T,
                    from: y,
                    to: x,
                    slides: function () {
                        for (var e = [], t = y; t <= x; t += 1) e.push(u[t]);
                        return e
                    }()
                }), void E();
                var S = [], C = [];
                if (e) t.$wrapperEl.find("." + t.params.slideClass).remove(); else for (var M = p; M <= c; M += 1) (M < y || x < M) && t.$wrapperEl.find("." + t.params.slideClass + '[data-swiper-slide-index="' + M + '"]').remove();
                for (var k = 0; k < u.length; k += 1) y <= k && k <= x && (void 0 === c || e ? C.push(k) : (c < k && C.push(k), k < p && S.push(k)));
                C.forEach(function (e) {
                    t.$wrapperEl.append(v(u[e], e))
                }), S.sort(function (e, t) {
                    return e < t
                }).forEach(function (e) {
                    t.$wrapperEl.prepend(v(u[e], e))
                }), t.$wrapperEl.children(".swiper-slide").css(m, T + "px"), E()
            }, renderSlide: function (e, t) {
                var a = this, i = a.params.virtual;
                if (i.cache && a.virtual.cache[t]) return a.virtual.cache[t];
                var s = i.renderSlide ? L(i.renderSlide.call(a, e, t)) : L('<div class="' + a.params.slideClass + '" data-swiper-slide-index="' + t + '">' + e + "</div>");
                return s.attr("data-swiper-slide-index") || s.attr("data-swiper-slide-index", t), i.cache && (a.virtual.cache[t] = s), s
            }, appendSlide: function (e) {
                this.virtual.slides.push(e), this.virtual.update(!0)
            }, prependSlide: function (e) {
                var t = this;
                if (t.virtual.slides.unshift(e), t.params.virtual.cache) {
                    var a = t.virtual.cache, i = {};
                    Object.keys(a).forEach(function (e) {
                        i[e + 1] = a[e]
                    }), t.virtual.cache = i
                }
                t.virtual.update(!0), t.slideNext(0)
            }
        }, O = {
            name: "virtual",
            params: {
                virtual: {
                    enabled: !1,
                    slides: [],
                    cache: !0,
                    renderSlide: null,
                    renderExternal: null,
                    addSlidesBefore: 0,
                    addSlidesAfter: 0
                }
            },
            create: function () {
                var e = this;
                V.extend(e, {
                    virtual: {
                        update: D.update.bind(e),
                        appendSlide: D.appendSlide.bind(e),
                        prependSlide: D.prependSlide.bind(e),
                        renderSlide: D.renderSlide.bind(e),
                        slides: e.params.virtual.slides,
                        cache: {}
                    }
                })
            },
            on: {
                beforeInit: function () {
                    var e = this;
                    if (e.params.virtual.enabled) {
                        e.classNames.push(e.params.containerModifierClass + "virtual");
                        var t = {watchSlidesProgress: !0};
                        V.extend(e.params, t), V.extend(e.originalParams, t), e.virtual.update()
                    }
                }, setTranslate: function () {
                    this.params.virtual.enabled && this.virtual.update()
                }
            }
        }, A = {
            handle: function (e) {
                var t = this, a = t.rtlTranslate, i = e;
                i.originalEvent && (i = i.originalEvent);
                var s = i.keyCode || i.charCode;
                if (!t.allowSlideNext && (t.isHorizontal() && 39 === s || t.isVertical() && 40 === s)) return !1;
                if (!t.allowSlidePrev && (t.isHorizontal() && 37 === s || t.isVertical() && 38 === s)) return !1;
                if (!(i.shiftKey || i.altKey || i.ctrlKey || i.metaKey || f.activeElement && f.activeElement.nodeName && ("input" === f.activeElement.nodeName.toLowerCase() || "textarea" === f.activeElement.nodeName.toLowerCase()))) {
                    if (t.params.keyboard.onlyInViewport && (37 === s || 39 === s || 38 === s || 40 === s)) {
                        var r = !1;
                        if (0 < t.$el.parents("." + t.params.slideClass).length && 0 === t.$el.parents("." + t.params.slideActiveClass).length) return;
                        var n = Y.innerWidth, o = Y.innerHeight, l = t.$el.offset();
                        a && (l.left -= t.$el[0].scrollLeft);
                        for (var d = [[l.left, l.top], [l.left + t.width, l.top], [l.left, l.top + t.height], [l.left + t.width, l.top + t.height]], p = 0; p < d.length; p += 1) {
                            var c = d[p];
                            0 <= c[0] && c[0] <= n && 0 <= c[1] && c[1] <= o && (r = !0)
                        }
                        if (!r) return
                    }
                    t.isHorizontal() ? (37 !== s && 39 !== s || (i.preventDefault ? i.preventDefault() : i.returnValue = !1), (39 === s && !a || 37 === s && a) && t.slideNext(), (37 === s && !a || 39 === s && a) && t.slidePrev()) : (38 !== s && 40 !== s || (i.preventDefault ? i.preventDefault() : i.returnValue = !1), 40 === s && t.slideNext(), 38 === s && t.slidePrev()), t.emit("keyPress", s)
                }
            }, enable: function () {
                this.keyboard.enabled || (L(f).on("keydown", this.keyboard.handle), this.keyboard.enabled = !0)
            }, disable: function () {
                this.keyboard.enabled && (L(f).off("keydown", this.keyboard.handle), this.keyboard.enabled = !1)
            }
        }, H = {
            name: "keyboard", params: {keyboard: {enabled: !1, onlyInViewport: !0}}, create: function () {
                V.extend(this, {
                    keyboard: {
                        enabled: !1,
                        enable: A.enable.bind(this),
                        disable: A.disable.bind(this),
                        handle: A.handle.bind(this)
                    }
                })
            }, on: {
                init: function () {
                    this.params.keyboard.enabled && this.keyboard.enable()
                }, destroy: function () {
                    this.keyboard.enabled && this.keyboard.disable()
                }
            }
        };
    var B = {
        lastScrollTime: V.now(), event: -1 < Y.navigator.userAgent.indexOf("firefox") ? "DOMMouseScroll" : function () {
            var e = "onwheel", t = e in f;
            if (!t) {
                var a = f.createElement("div");
                a.setAttribute(e, "return;"), t = "function" == typeof a[e]
            }
            return !t && f.implementation && f.implementation.hasFeature && !0 !== f.implementation.hasFeature("", "") && (t = f.implementation.hasFeature("Events.wheel", "3.0")), t
        }() ? "wheel" : "mousewheel", normalize: function (e) {
            var t = 0, a = 0, i = 0, s = 0;
            return "detail" in e && (a = e.detail), "wheelDelta" in e && (a = -e.wheelDelta / 120), "wheelDeltaY" in e && (a = -e.wheelDeltaY / 120), "wheelDeltaX" in e && (t = -e.wheelDeltaX / 120), "axis" in e && e.axis === e.HORIZONTAL_AXIS && (t = a, a = 0), i = 10 * t, s = 10 * a, "deltaY" in e && (s = e.deltaY), "deltaX" in e && (i = e.deltaX), (i || s) && e.deltaMode && (1 === e.deltaMode ? (i *= 40, s *= 40) : (i *= 800, s *= 800)), i && !t && (t = i < 1 ? -1 : 1), s && !a && (a = s < 1 ? -1 : 1), {
                spinX: t,
                spinY: a,
                pixelX: i,
                pixelY: s
            }
        }, handleMouseEnter: function () {
            this.mouseEntered = !0
        }, handleMouseLeave: function () {
            this.mouseEntered = !1
        }, handle: function (e) {
            var t = e, a = this, i = a.params.mousewheel;
            if (!a.mouseEntered && !i.releaseOnEdges) return !0;
            t.originalEvent && (t = t.originalEvent);
            var s = 0, r = a.rtlTranslate ? -1 : 1, n = B.normalize(t);
            if (i.forceToAxis) if (a.isHorizontal()) {
                if (!(Math.abs(n.pixelX) > Math.abs(n.pixelY))) return !0;
                s = n.pixelX * r
            } else {
                if (!(Math.abs(n.pixelY) > Math.abs(n.pixelX))) return !0;
                s = n.pixelY
            } else s = Math.abs(n.pixelX) > Math.abs(n.pixelY) ? -n.pixelX * r : -n.pixelY;
            if (0 === s) return !0;
            if (i.invert && (s = -s), a.params.freeMode) {
                a.params.loop && a.loopFix();
                var o = a.getTranslate() + s * i.sensitivity, l = a.isBeginning, d = a.isEnd;
                if (o >= a.minTranslate() && (o = a.minTranslate()), o <= a.maxTranslate() && (o = a.maxTranslate()), a.setTransition(0), a.setTranslate(o), a.updateProgress(), a.updateActiveIndex(), a.updateSlidesClasses(), (!l && a.isBeginning || !d && a.isEnd) && a.updateSlidesClasses(), a.params.freeModeSticky && (clearTimeout(a.mousewheel.timeout), a.mousewheel.timeout = V.nextTick(function () {
                    a.slideToClosest()
                }, 300)), a.emit("scroll", t), a.params.autoplay && a.params.autoplayDisableOnInteraction && a.autoplay.stop(), o === a.minTranslate() || o === a.maxTranslate()) return !0
            } else {
                if (60 < V.now() - a.mousewheel.lastScrollTime) if (s < 0) if (a.isEnd && !a.params.loop || a.animating) {
                    if (i.releaseOnEdges) return !0
                } else a.slideNext(), a.emit("scroll", t); else if (a.isBeginning && !a.params.loop || a.animating) {
                    if (i.releaseOnEdges) return !0
                } else a.slidePrev(), a.emit("scroll", t);
                a.mousewheel.lastScrollTime = (new Y.Date).getTime()
            }
            return t.preventDefault ? t.preventDefault() : t.returnValue = !1, !1
        }, enable: function () {
            var e = this;
            if (!B.event) return !1;
            if (e.mousewheel.enabled) return !1;
            var t = e.$el;
            return "container" !== e.params.mousewheel.eventsTarged && (t = L(e.params.mousewheel.eventsTarged)), t.on("mouseenter", e.mousewheel.handleMouseEnter), t.on("mouseleave", e.mousewheel.handleMouseLeave), t.on(B.event, e.mousewheel.handle), e.mousewheel.enabled = !0
        }, disable: function () {
            var e = this;
            if (!B.event) return !1;
            if (!e.mousewheel.enabled) return !1;
            var t = e.$el;
            return "container" !== e.params.mousewheel.eventsTarged && (t = L(e.params.mousewheel.eventsTarged)), t.off(B.event, e.mousewheel.handle), !(e.mousewheel.enabled = !1)
        }
    }, G = {
        update: function () {
            var e = this, t = e.params.navigation;
            if (!e.params.loop) {
                var a = e.navigation, i = a.$nextEl, s = a.$prevEl;
                s && 0 < s.length && (e.isBeginning ? s.addClass(t.disabledClass) : s.removeClass(t.disabledClass), s[e.params.watchOverflow && e.isLocked ? "addClass" : "removeClass"](t.lockClass)), i && 0 < i.length && (e.isEnd ? i.addClass(t.disabledClass) : i.removeClass(t.disabledClass), i[e.params.watchOverflow && e.isLocked ? "addClass" : "removeClass"](t.lockClass))
            }
        }, init: function () {
            var e, t, a = this, i = a.params.navigation;
            (i.nextEl || i.prevEl) && (i.nextEl && (e = L(i.nextEl), a.params.uniqueNavElements && "string" == typeof i.nextEl && 1 < e.length && 1 === a.$el.find(i.nextEl).length && (e = a.$el.find(i.nextEl))), i.prevEl && (t = L(i.prevEl), a.params.uniqueNavElements && "string" == typeof i.prevEl && 1 < t.length && 1 === a.$el.find(i.prevEl).length && (t = a.$el.find(i.prevEl))), e && 0 < e.length && e.on("click", function (e) {
                e.preventDefault(), a.isEnd && !a.params.loop || a.slideNext()
            }), t && 0 < t.length && t.on("click", function (e) {
                e.preventDefault(), a.isBeginning && !a.params.loop || a.slidePrev()
            }), V.extend(a.navigation, {$nextEl: e, nextEl: e && e[0], $prevEl: t, prevEl: t && t[0]}))
        }, destroy: function () {
            var e = this.navigation, t = e.$nextEl, a = e.$prevEl;
            t && t.length && (t.off("click"), t.removeClass(this.params.navigation.disabledClass)), a && a.length && (a.off("click"), a.removeClass(this.params.navigation.disabledClass))
        }
    }, N = {
        update: function () {
            var e = this, t = e.rtl, s = e.params.pagination;
            if (s.el && e.pagination.el && e.pagination.$el && 0 !== e.pagination.$el.length) {
                var r, a = e.virtual && e.params.virtual.enabled ? e.virtual.slides.length : e.slides.length,
                    i = e.pagination.$el,
                    n = e.params.loop ? Math.ceil((a - 2 * e.loopedSlides) / e.params.slidesPerGroup) : e.snapGrid.length;
                if (e.params.loop ? ((r = Math.ceil((e.activeIndex - e.loopedSlides) / e.params.slidesPerGroup)) > a - 1 - 2 * e.loopedSlides && (r -= a - 2 * e.loopedSlides), n - 1 < r && (r -= n), r < 0 && "bullets" !== e.params.paginationType && (r = n + r)) : r = void 0 !== e.snapIndex ? e.snapIndex : e.activeIndex || 0, "bullets" === s.type && e.pagination.bullets && 0 < e.pagination.bullets.length) {
                    var o, l, d, p = e.pagination.bullets;
                    if (s.dynamicBullets && (e.pagination.bulletSize = p.eq(0)[e.isHorizontal() ? "outerWidth" : "outerHeight"](!0), i.css(e.isHorizontal() ? "width" : "height", e.pagination.bulletSize * (s.dynamicMainBullets + 4) + "px"), 1 < s.dynamicMainBullets && void 0 !== e.previousIndex && (e.pagination.dynamicBulletIndex += r - e.previousIndex, e.pagination.dynamicBulletIndex > s.dynamicMainBullets - 1 ? e.pagination.dynamicBulletIndex = s.dynamicMainBullets - 1 : e.pagination.dynamicBulletIndex < 0 && (e.pagination.dynamicBulletIndex = 0)), o = r - e.pagination.dynamicBulletIndex, d = ((l = o + (Math.min(p.length, s.dynamicMainBullets) - 1)) + o) / 2), p.removeClass(s.bulletActiveClass + " " + s.bulletActiveClass + "-next " + s.bulletActiveClass + "-next-next " + s.bulletActiveClass + "-prev " + s.bulletActiveClass + "-prev-prev " + s.bulletActiveClass + "-main"), 1 < i.length) p.each(function (e, t) {
                        var a = L(t), i = a.index();
                        i === r && a.addClass(s.bulletActiveClass), s.dynamicBullets && (o <= i && i <= l && a.addClass(s.bulletActiveClass + "-main"), i === o && a.prev().addClass(s.bulletActiveClass + "-prev").prev().addClass(s.bulletActiveClass + "-prev-prev"), i === l && a.next().addClass(s.bulletActiveClass + "-next").next().addClass(s.bulletActiveClass + "-next-next"))
                    }); else if (p.eq(r).addClass(s.bulletActiveClass), s.dynamicBullets) {
                        for (var c = p.eq(o), u = p.eq(l), h = o; h <= l; h += 1) p.eq(h).addClass(s.bulletActiveClass + "-main");
                        c.prev().addClass(s.bulletActiveClass + "-prev").prev().addClass(s.bulletActiveClass + "-prev-prev"), u.next().addClass(s.bulletActiveClass + "-next").next().addClass(s.bulletActiveClass + "-next-next")
                    }
                    if (s.dynamicBullets) {
                        var v = Math.min(p.length, s.dynamicMainBullets + 4),
                            f = (e.pagination.bulletSize * v - e.pagination.bulletSize) / 2 - d * e.pagination.bulletSize,
                            m = t ? "right" : "left";
                        p.css(e.isHorizontal() ? m : "top", f + "px")
                    }
                }
                if ("fraction" === s.type && (i.find("." + s.currentClass).text(s.formatFractionCurrent(r + 1)), i.find("." + s.totalClass).text(s.formatFractionTotal(n))), "progressbar" === s.type) {
                    var g;
                    g = s.progressbarOpposite ? e.isHorizontal() ? "vertical" : "horizontal" : e.isHorizontal() ? "horizontal" : "vertical";
                    var b = (r + 1) / n, w = 1, y = 1;
                    "horizontal" === g ? w = b : y = b, i.find("." + s.progressbarFillClass).transform("translate3d(0,0,0) scaleX(" + w + ") scaleY(" + y + ")").transition(e.params.speed)
                }
                "custom" === s.type && s.renderCustom ? (i.html(s.renderCustom(e, r + 1, n)), e.emit("paginationRender", e, i[0])) : e.emit("paginationUpdate", e, i[0]), i[e.params.watchOverflow && e.isLocked ? "addClass" : "removeClass"](s.lockClass)
            }
        }, render: function () {
            var e = this, t = e.params.pagination;
            if (t.el && e.pagination.el && e.pagination.$el && 0 !== e.pagination.$el.length) {
                var a = e.virtual && e.params.virtual.enabled ? e.virtual.slides.length : e.slides.length,
                    i = e.pagination.$el, s = "";
                if ("bullets" === t.type) {
                    for (var r = e.params.loop ? Math.ceil((a - 2 * e.loopedSlides) / e.params.slidesPerGroup) : e.snapGrid.length, n = 0; n < r; n += 1) t.renderBullet ? s += t.renderBullet.call(e, n, t.bulletClass) : s += "<" + t.bulletElement + ' class="' + t.bulletClass + '"></' + t.bulletElement + ">";
                    i.html(s), e.pagination.bullets = i.find("." + t.bulletClass)
                }
                "fraction" === t.type && (s = t.renderFraction ? t.renderFraction.call(e, t.currentClass, t.totalClass) : '<span class="' + t.currentClass + '"></span> / <span class="' + t.totalClass + '"></span>', i.html(s)), "progressbar" === t.type && (s = t.renderProgressbar ? t.renderProgressbar.call(e, t.progressbarFillClass) : '<span class="' + t.progressbarFillClass + '"></span>', i.html(s)), "custom" !== t.type && e.emit("paginationRender", e.pagination.$el[0])
            }
        }, init: function () {
            var a = this, e = a.params.pagination;
            if (e.el) {
                var t = L(e.el);
                0 !== t.length && (a.params.uniqueNavElements && "string" == typeof e.el && 1 < t.length && 1 === a.$el.find(e.el).length && (t = a.$el.find(e.el)), "bullets" === e.type && e.clickable && t.addClass(e.clickableClass), t.addClass(e.modifierClass + e.type), "bullets" === e.type && e.dynamicBullets && (t.addClass("" + e.modifierClass + e.type + "-dynamic"), a.pagination.dynamicBulletIndex = 0, e.dynamicMainBullets < 1 && (e.dynamicMainBullets = 1)), "progressbar" === e.type && e.progressbarOpposite && t.addClass(e.progressbarOppositeClass), e.clickable && t.on("click", "." + e.bulletClass, function (e) {
                    e.preventDefault();
                    var t = L(this).index() * a.params.slidesPerGroup;
                    a.params.loop && (t += a.loopedSlides), a.slideTo(t)
                }), V.extend(a.pagination, {$el: t, el: t[0]}))
            }
        }, destroy: function () {
            var e = this, t = e.params.pagination;
            if (t.el && e.pagination.el && e.pagination.$el && 0 !== e.pagination.$el.length) {
                var a = e.pagination.$el;
                a.removeClass(t.hiddenClass), a.removeClass(t.modifierClass + t.type), e.pagination.bullets && e.pagination.bullets.removeClass(t.bulletActiveClass), t.clickable && a.off("click", "." + t.bulletClass)
            }
        }
    }, X = {
        setTranslate: function () {
            var e = this;
            if (e.params.scrollbar.el && e.scrollbar.el) {
                var t = e.scrollbar, a = e.rtlTranslate, i = e.progress, s = t.dragSize, r = t.trackSize, n = t.$dragEl,
                    o = t.$el, l = e.params.scrollbar, d = s, p = (r - s) * i;
                a ? 0 < (p = -p) ? (d = s - p, p = 0) : r < -p + s && (d = r + p) : p < 0 ? (d = s + p, p = 0) : r < p + s && (d = r - p), e.isHorizontal() ? (R.transforms3d ? n.transform("translate3d(" + p + "px, 0, 0)") : n.transform("translateX(" + p + "px)"), n[0].style.width = d + "px") : (R.transforms3d ? n.transform("translate3d(0px, " + p + "px, 0)") : n.transform("translateY(" + p + "px)"), n[0].style.height = d + "px"), l.hide && (clearTimeout(e.scrollbar.timeout), o[0].style.opacity = 1, e.scrollbar.timeout = setTimeout(function () {
                    o[0].style.opacity = 0, o.transition(400)
                }, 1e3))
            }
        }, setTransition: function (e) {
            this.params.scrollbar.el && this.scrollbar.el && this.scrollbar.$dragEl.transition(e)
        }, updateSize: function () {
            var e = this;
            if (e.params.scrollbar.el && e.scrollbar.el) {
                var t = e.scrollbar, a = t.$dragEl, i = t.$el;
                a[0].style.width = "", a[0].style.height = "";
                var s, r = e.isHorizontal() ? i[0].offsetWidth : i[0].offsetHeight, n = e.size / e.virtualSize,
                    o = n * (r / e.size);
                s = "auto" === e.params.scrollbar.dragSize ? r * n : parseInt(e.params.scrollbar.dragSize, 10), e.isHorizontal() ? a[0].style.width = s + "px" : a[0].style.height = s + "px", i[0].style.display = 1 <= n ? "none" : "", e.params.scrollbarHide && (i[0].style.opacity = 0), V.extend(t, {
                    trackSize: r,
                    divider: n,
                    moveDivider: o,
                    dragSize: s
                }), t.$el[e.params.watchOverflow && e.isLocked ? "addClass" : "removeClass"](e.params.scrollbar.lockClass)
            }
        }, setDragPosition: function (e) {
            var t, a = this, i = a.scrollbar, s = a.rtlTranslate, r = i.$el, n = i.dragSize, o = i.trackSize;
            t = ((a.isHorizontal() ? "touchstart" === e.type || "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX || e.clientX : "touchstart" === e.type || "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY || e.clientY) - r.offset()[a.isHorizontal() ? "left" : "top"] - n / 2) / (o - n), t = Math.max(Math.min(t, 1), 0), s && (t = 1 - t);
            var l = a.minTranslate() + (a.maxTranslate() - a.minTranslate()) * t;
            a.updateProgress(l), a.setTranslate(l), a.updateActiveIndex(), a.updateSlidesClasses()
        }, onDragStart: function (e) {
            var t = this, a = t.params.scrollbar, i = t.scrollbar, s = t.$wrapperEl, r = i.$el, n = i.$dragEl;
            t.scrollbar.isTouched = !0, e.preventDefault(), e.stopPropagation(), s.transition(100), n.transition(100), i.setDragPosition(e), clearTimeout(t.scrollbar.dragTimeout), r.transition(0), a.hide && r.css("opacity", 1), t.emit("scrollbarDragStart", e)
        }, onDragMove: function (e) {
            var t = this.scrollbar, a = this.$wrapperEl, i = t.$el, s = t.$dragEl;
            this.scrollbar.isTouched && (e.preventDefault ? e.preventDefault() : e.returnValue = !1, t.setDragPosition(e), a.transition(0), i.transition(0), s.transition(0), this.emit("scrollbarDragMove", e))
        }, onDragEnd: function (e) {
            var t = this, a = t.params.scrollbar, i = t.scrollbar.$el;
            t.scrollbar.isTouched && (t.scrollbar.isTouched = !1, a.hide && (clearTimeout(t.scrollbar.dragTimeout), t.scrollbar.dragTimeout = V.nextTick(function () {
                i.css("opacity", 0), i.transition(400)
            }, 1e3)), t.emit("scrollbarDragEnd", e), a.snapOnRelease && t.slideToClosest())
        }, enableDraggable: function () {
            var e = this;
            if (e.params.scrollbar.el) {
                var t = e.scrollbar, a = e.touchEvents, i = e.touchEventsDesktop, s = e.params, r = t.$el[0],
                    n = !(!R.passiveListener || !s.passiveListeners) && {passive: !1, capture: !1},
                    o = !(!R.passiveListener || !s.passiveListeners) && {passive: !0, capture: !1};
                R.touch || !R.pointerEvents && !R.prefixedPointerEvents ? (R.touch && (r.addEventListener(a.start, e.scrollbar.onDragStart, n), r.addEventListener(a.move, e.scrollbar.onDragMove, n), r.addEventListener(a.end, e.scrollbar.onDragEnd, o)), (s.simulateTouch && !m.ios && !m.android || s.simulateTouch && !R.touch && m.ios) && (r.addEventListener("mousedown", e.scrollbar.onDragStart, n), f.addEventListener("mousemove", e.scrollbar.onDragMove, n), f.addEventListener("mouseup", e.scrollbar.onDragEnd, o))) : (r.addEventListener(i.start, e.scrollbar.onDragStart, n), f.addEventListener(i.move, e.scrollbar.onDragMove, n), f.addEventListener(i.end, e.scrollbar.onDragEnd, o))
            }
        }, disableDraggable: function () {
            var e = this;
            if (e.params.scrollbar.el) {
                var t = e.scrollbar, a = e.touchEvents, i = e.touchEventsDesktop, s = e.params, r = t.$el[0],
                    n = !(!R.passiveListener || !s.passiveListeners) && {passive: !1, capture: !1},
                    o = !(!R.passiveListener || !s.passiveListeners) && {passive: !0, capture: !1};
                R.touch || !R.pointerEvents && !R.prefixedPointerEvents ? (R.touch && (r.removeEventListener(a.start, e.scrollbar.onDragStart, n), r.removeEventListener(a.move, e.scrollbar.onDragMove, n), r.removeEventListener(a.end, e.scrollbar.onDragEnd, o)), (s.simulateTouch && !m.ios && !m.android || s.simulateTouch && !R.touch && m.ios) && (r.removeEventListener("mousedown", e.scrollbar.onDragStart, n), f.removeEventListener("mousemove", e.scrollbar.onDragMove, n), f.removeEventListener("mouseup", e.scrollbar.onDragEnd, o))) : (r.removeEventListener(i.start, e.scrollbar.onDragStart, n), f.removeEventListener(i.move, e.scrollbar.onDragMove, n), f.removeEventListener(i.end, e.scrollbar.onDragEnd, o))
            }
        }, init: function () {
            var e = this;
            if (e.params.scrollbar.el) {
                var t = e.scrollbar, a = e.$el, i = e.params.scrollbar, s = L(i.el);
                e.params.uniqueNavElements && "string" == typeof i.el && 1 < s.length && 1 === a.find(i.el).length && (s = a.find(i.el));
                var r = s.find("." + e.params.scrollbar.dragClass);
                0 === r.length && (r = L('<div class="' + e.params.scrollbar.dragClass + '"></div>'), s.append(r)), V.extend(t, {
                    $el: s,
                    el: s[0],
                    $dragEl: r,
                    dragEl: r[0]
                }), i.draggable && t.enableDraggable()
            }
        }, destroy: function () {
            this.scrollbar.disableDraggable()
        }
    }, F = {
        setTransform: function (e, t) {
            var a = this.rtl, i = L(e), s = a ? -1 : 1, r = i.attr("data-swiper-parallax") || "0",
                n = i.attr("data-swiper-parallax-x"), o = i.attr("data-swiper-parallax-y"),
                l = i.attr("data-swiper-parallax-scale"), d = i.attr("data-swiper-parallax-opacity");
            if (n || o ? (n = n || "0", o = o || "0") : this.isHorizontal() ? (n = r, o = "0") : (o = r, n = "0"), n = 0 <= n.indexOf("%") ? parseInt(n, 10) * t * s + "%" : n * t * s + "px", o = 0 <= o.indexOf("%") ? parseInt(o, 10) * t + "%" : o * t + "px", null != d) {
                var p = d - (d - 1) * (1 - Math.abs(t));
                i[0].style.opacity = p
            }
            if (null == l) i.transform("translate3d(" + n + ", " + o + ", 0px)"); else {
                var c = l - (l - 1) * (1 - Math.abs(t));
                i.transform("translate3d(" + n + ", " + o + ", 0px) scale(" + c + ")")
            }
        }, setTranslate: function () {
            var i = this, e = i.$el, t = i.slides, s = i.progress, r = i.snapGrid;
            e.children("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function (e, t) {
                i.parallax.setTransform(t, s)
            }), t.each(function (e, t) {
                var a = t.progress;
                1 < i.params.slidesPerGroup && "auto" !== i.params.slidesPerView && (a += Math.ceil(e / 2) - s * (r.length - 1)), a = Math.min(Math.max(a, -1), 1), L(t).find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function (e, t) {
                    i.parallax.setTransform(t, a)
                })
            })
        }, setTransition: function (s) {
            void 0 === s && (s = this.params.speed);
            this.$el.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function (e, t) {
                var a = L(t), i = parseInt(a.attr("data-swiper-parallax-duration"), 10) || s;
                0 === s && (i = 0), a.transition(i)
            })
        }
    }, q = {
        getDistanceBetweenTouches: function (e) {
            if (e.targetTouches.length < 2) return 1;
            var t = e.targetTouches[0].pageX, a = e.targetTouches[0].pageY, i = e.targetTouches[1].pageX,
                s = e.targetTouches[1].pageY;
            return Math.sqrt(Math.pow(i - t, 2) + Math.pow(s - a, 2))
        }, onGestureStart: function (e) {
            var t = this, a = t.params.zoom, i = t.zoom, s = i.gesture;
            if (i.fakeGestureTouched = !1, i.fakeGestureMoved = !1, !R.gestures) {
                if ("touchstart" !== e.type || "touchstart" === e.type && e.targetTouches.length < 2) return;
                i.fakeGestureTouched = !0, s.scaleStart = q.getDistanceBetweenTouches(e)
            }
            s.$slideEl && s.$slideEl.length || (s.$slideEl = L(e.target).closest(".swiper-slide"), 0 === s.$slideEl.length && (s.$slideEl = t.slides.eq(t.activeIndex)), s.$imageEl = s.$slideEl.find("img, svg, canvas"), s.$imageWrapEl = s.$imageEl.parent("." + a.containerClass), s.maxRatio = s.$imageWrapEl.attr("data-swiper-zoom") || a.maxRatio, 0 !== s.$imageWrapEl.length) ? (s.$imageEl.transition(0), t.zoom.isScaling = !0) : s.$imageEl = void 0
        }, onGestureChange: function (e) {
            var t = this.params.zoom, a = this.zoom, i = a.gesture;
            if (!R.gestures) {
                if ("touchmove" !== e.type || "touchmove" === e.type && e.targetTouches.length < 2) return;
                a.fakeGestureMoved = !0, i.scaleMove = q.getDistanceBetweenTouches(e)
            }
            i.$imageEl && 0 !== i.$imageEl.length && (R.gestures ? this.zoom.scale = e.scale * a.currentScale : a.scale = i.scaleMove / i.scaleStart * a.currentScale, a.scale > i.maxRatio && (a.scale = i.maxRatio - 1 + Math.pow(a.scale - i.maxRatio + 1, .5)), a.scale < t.minRatio && (a.scale = t.minRatio + 1 - Math.pow(t.minRatio - a.scale + 1, .5)), i.$imageEl.transform("translate3d(0,0,0) scale(" + a.scale + ")"))
        }, onGestureEnd: function (e) {
            var t = this.params.zoom, a = this.zoom, i = a.gesture;
            if (!R.gestures) {
                if (!a.fakeGestureTouched || !a.fakeGestureMoved) return;
                if ("touchend" !== e.type || "touchend" === e.type && e.changedTouches.length < 2 && !m.android) return;
                a.fakeGestureTouched = !1, a.fakeGestureMoved = !1
            }
            i.$imageEl && 0 !== i.$imageEl.length && (a.scale = Math.max(Math.min(a.scale, i.maxRatio), t.minRatio), i.$imageEl.transition(this.params.speed).transform("translate3d(0,0,0) scale(" + a.scale + ")"), a.currentScale = a.scale, a.isScaling = !1, 1 === a.scale && (i.$slideEl = void 0))
        }, onTouchStart: function (e) {
            var t = this.zoom, a = t.gesture, i = t.image;
            a.$imageEl && 0 !== a.$imageEl.length && (i.isTouched || (m.android && e.preventDefault(), i.isTouched = !0, i.touchesStart.x = "touchstart" === e.type ? e.targetTouches[0].pageX : e.pageX, i.touchesStart.y = "touchstart" === e.type ? e.targetTouches[0].pageY : e.pageY))
        }, onTouchMove: function (e) {
            var t = this, a = t.zoom, i = a.gesture, s = a.image, r = a.velocity;
            if (i.$imageEl && 0 !== i.$imageEl.length && (t.allowClick = !1, s.isTouched && i.$slideEl)) {
                s.isMoved || (s.width = i.$imageEl[0].offsetWidth, s.height = i.$imageEl[0].offsetHeight, s.startX = V.getTranslate(i.$imageWrapEl[0], "x") || 0, s.startY = V.getTranslate(i.$imageWrapEl[0], "y") || 0, i.slideWidth = i.$slideEl[0].offsetWidth, i.slideHeight = i.$slideEl[0].offsetHeight, i.$imageWrapEl.transition(0), t.rtl && (s.startX = -s.startX, s.startY = -s.startY));
                var n = s.width * a.scale, o = s.height * a.scale;
                if (!(n < i.slideWidth && o < i.slideHeight)) {
                    if (s.minX = Math.min(i.slideWidth / 2 - n / 2, 0), s.maxX = -s.minX, s.minY = Math.min(i.slideHeight / 2 - o / 2, 0), s.maxY = -s.minY, s.touchesCurrent.x = "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX, s.touchesCurrent.y = "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY, !s.isMoved && !a.isScaling) {
                        if (t.isHorizontal() && (Math.floor(s.minX) === Math.floor(s.startX) && s.touchesCurrent.x < s.touchesStart.x || Math.floor(s.maxX) === Math.floor(s.startX) && s.touchesCurrent.x > s.touchesStart.x)) return void (s.isTouched = !1);
                        if (!t.isHorizontal() && (Math.floor(s.minY) === Math.floor(s.startY) && s.touchesCurrent.y < s.touchesStart.y || Math.floor(s.maxY) === Math.floor(s.startY) && s.touchesCurrent.y > s.touchesStart.y)) return void (s.isTouched = !1)
                    }
                    e.preventDefault(), e.stopPropagation(), s.isMoved = !0, s.currentX = s.touchesCurrent.x - s.touchesStart.x + s.startX, s.currentY = s.touchesCurrent.y - s.touchesStart.y + s.startY, s.currentX < s.minX && (s.currentX = s.minX + 1 - Math.pow(s.minX - s.currentX + 1, .8)), s.currentX > s.maxX && (s.currentX = s.maxX - 1 + Math.pow(s.currentX - s.maxX + 1, .8)), s.currentY < s.minY && (s.currentY = s.minY + 1 - Math.pow(s.minY - s.currentY + 1, .8)), s.currentY > s.maxY && (s.currentY = s.maxY - 1 + Math.pow(s.currentY - s.maxY + 1, .8)), r.prevPositionX || (r.prevPositionX = s.touchesCurrent.x), r.prevPositionY || (r.prevPositionY = s.touchesCurrent.y), r.prevTime || (r.prevTime = Date.now()), r.x = (s.touchesCurrent.x - r.prevPositionX) / (Date.now() - r.prevTime) / 2, r.y = (s.touchesCurrent.y - r.prevPositionY) / (Date.now() - r.prevTime) / 2, Math.abs(s.touchesCurrent.x - r.prevPositionX) < 2 && (r.x = 0), Math.abs(s.touchesCurrent.y - r.prevPositionY) < 2 && (r.y = 0), r.prevPositionX = s.touchesCurrent.x, r.prevPositionY = s.touchesCurrent.y, r.prevTime = Date.now(), i.$imageWrapEl.transform("translate3d(" + s.currentX + "px, " + s.currentY + "px,0)")
                }
            }
        }, onTouchEnd: function () {
            var e = this.zoom, t = e.gesture, a = e.image, i = e.velocity;
            if (t.$imageEl && 0 !== t.$imageEl.length) {
                if (!a.isTouched || !a.isMoved) return a.isTouched = !1, void (a.isMoved = !1);
                a.isTouched = !1, a.isMoved = !1;
                var s = 300, r = 300, n = i.x * s, o = a.currentX + n, l = i.y * r, d = a.currentY + l;
                0 !== i.x && (s = Math.abs((o - a.currentX) / i.x)), 0 !== i.y && (r = Math.abs((d - a.currentY) / i.y));
                var p = Math.max(s, r);
                a.currentX = o, a.currentY = d;
                var c = a.width * e.scale, u = a.height * e.scale;
                a.minX = Math.min(t.slideWidth / 2 - c / 2, 0), a.maxX = -a.minX, a.minY = Math.min(t.slideHeight / 2 - u / 2, 0), a.maxY = -a.minY, a.currentX = Math.max(Math.min(a.currentX, a.maxX), a.minX), a.currentY = Math.max(Math.min(a.currentY, a.maxY), a.minY), t.$imageWrapEl.transition(p).transform("translate3d(" + a.currentX + "px, " + a.currentY + "px,0)")
            }
        }, onTransitionEnd: function () {
            var e = this.zoom, t = e.gesture;
            t.$slideEl && this.previousIndex !== this.activeIndex && (t.$imageEl.transform("translate3d(0,0,0) scale(1)"), t.$imageWrapEl.transform("translate3d(0,0,0)"), t.$slideEl = void 0, t.$imageEl = void 0, t.$imageWrapEl = void 0, e.scale = 1, e.currentScale = 1)
        }, toggle: function (e) {
            var t = this.zoom;
            t.scale && 1 !== t.scale ? t.out() : t.in(e)
        }, in: function (e) {
            var t, a, i, s, r, n, o, l, d, p, c, u, h, v, f, m, g = this, b = g.zoom, w = g.params.zoom, y = b.gesture,
                x = b.image;
            (y.$slideEl || (y.$slideEl = g.clickedSlide ? L(g.clickedSlide) : g.slides.eq(g.activeIndex), y.$imageEl = y.$slideEl.find("img, svg, canvas"), y.$imageWrapEl = y.$imageEl.parent("." + w.containerClass)), y.$imageEl && 0 !== y.$imageEl.length) && (y.$slideEl.addClass("" + w.zoomedSlideClass), void 0 === x.touchesStart.x && e ? (t = "touchend" === e.type ? e.changedTouches[0].pageX : e.pageX, a = "touchend" === e.type ? e.changedTouches[0].pageY : e.pageY) : (t = x.touchesStart.x, a = x.touchesStart.y), b.scale = y.$imageWrapEl.attr("data-swiper-zoom") || w.maxRatio, b.currentScale = y.$imageWrapEl.attr("data-swiper-zoom") || w.maxRatio, e ? (f = y.$slideEl[0].offsetWidth, m = y.$slideEl[0].offsetHeight, i = y.$slideEl.offset().left + f / 2 - t, s = y.$slideEl.offset().top + m / 2 - a, o = y.$imageEl[0].offsetWidth, l = y.$imageEl[0].offsetHeight, d = o * b.scale, p = l * b.scale, h = -(c = Math.min(f / 2 - d / 2, 0)), v = -(u = Math.min(m / 2 - p / 2, 0)), (r = i * b.scale) < c && (r = c), h < r && (r = h), (n = s * b.scale) < u && (n = u), v < n && (n = v)) : n = r = 0, y.$imageWrapEl.transition(300).transform("translate3d(" + r + "px, " + n + "px,0)"), y.$imageEl.transition(300).transform("translate3d(0,0,0) scale(" + b.scale + ")"))
        }, out: function () {
            var e = this, t = e.zoom, a = e.params.zoom, i = t.gesture;
            i.$slideEl || (i.$slideEl = e.clickedSlide ? L(e.clickedSlide) : e.slides.eq(e.activeIndex), i.$imageEl = i.$slideEl.find("img, svg, canvas"), i.$imageWrapEl = i.$imageEl.parent("." + a.containerClass)), i.$imageEl && 0 !== i.$imageEl.length && (t.scale = 1, t.currentScale = 1, i.$imageWrapEl.transition(300).transform("translate3d(0,0,0)"), i.$imageEl.transition(300).transform("translate3d(0,0,0) scale(1)"), i.$slideEl.removeClass("" + a.zoomedSlideClass), i.$slideEl = void 0)
        }, enable: function () {
            var e = this, t = e.zoom;
            if (!t.enabled) {
                t.enabled = !0;
                var a = !("touchstart" !== e.touchEvents.start || !R.passiveListener || !e.params.passiveListeners) && {
                    passive: !0,
                    capture: !1
                };
                R.gestures ? (e.$wrapperEl.on("gesturestart", ".swiper-slide", t.onGestureStart, a), e.$wrapperEl.on("gesturechange", ".swiper-slide", t.onGestureChange, a), e.$wrapperEl.on("gestureend", ".swiper-slide", t.onGestureEnd, a)) : "touchstart" === e.touchEvents.start && (e.$wrapperEl.on(e.touchEvents.start, ".swiper-slide", t.onGestureStart, a), e.$wrapperEl.on(e.touchEvents.move, ".swiper-slide", t.onGestureChange, a), e.$wrapperEl.on(e.touchEvents.end, ".swiper-slide", t.onGestureEnd, a)), e.$wrapperEl.on(e.touchEvents.move, "." + e.params.zoom.containerClass, t.onTouchMove)
            }
        }, disable: function () {
            var e = this, t = e.zoom;
            if (t.enabled) {
                e.zoom.enabled = !1;
                var a = !("touchstart" !== e.touchEvents.start || !R.passiveListener || !e.params.passiveListeners) && {
                    passive: !0,
                    capture: !1
                };
                R.gestures ? (e.$wrapperEl.off("gesturestart", ".swiper-slide", t.onGestureStart, a), e.$wrapperEl.off("gesturechange", ".swiper-slide", t.onGestureChange, a), e.$wrapperEl.off("gestureend", ".swiper-slide", t.onGestureEnd, a)) : "touchstart" === e.touchEvents.start && (e.$wrapperEl.off(e.touchEvents.start, ".swiper-slide", t.onGestureStart, a), e.$wrapperEl.off(e.touchEvents.move, ".swiper-slide", t.onGestureChange, a), e.$wrapperEl.off(e.touchEvents.end, ".swiper-slide", t.onGestureEnd, a)), e.$wrapperEl.off(e.touchEvents.move, "." + e.params.zoom.containerClass, t.onTouchMove)
            }
        }
    }, W = {
        loadInSlide: function (e, l) {
            void 0 === l && (l = !0);
            var d = this, p = d.params.lazy;
            if (void 0 !== e && 0 !== d.slides.length) {
                var c = d.virtual && d.params.virtual.enabled ? d.$wrapperEl.children("." + d.params.slideClass + '[data-swiper-slide-index="' + e + '"]') : d.slides.eq(e),
                    t = c.find("." + p.elementClass + ":not(." + p.loadedClass + "):not(." + p.loadingClass + ")");
                !c.hasClass(p.elementClass) || c.hasClass(p.loadedClass) || c.hasClass(p.loadingClass) || (t = t.add(c[0])), 0 !== t.length && t.each(function (e, t) {
                    var i = L(t);
                    i.addClass(p.loadingClass);
                    var s = i.attr("data-background"), r = i.attr("data-src"), n = i.attr("data-srcset"),
                        o = i.attr("data-sizes");
                    d.loadImage(i[0], r || s, n, o, !1, function () {
                        if (null != d && d && (!d || d.params) && !d.destroyed) {
                            if (s ? (i.css("background-image", 'url("' + s + '")'), i.removeAttr("data-background")) : (n && (i.attr("srcset", n), i.removeAttr("data-srcset")), o && (i.attr("sizes", o), i.removeAttr("data-sizes")), r && (i.attr("src", r), i.removeAttr("data-src"))), i.addClass(p.loadedClass).removeClass(p.loadingClass), c.find("." + p.preloaderClass).remove(), d.params.loop && l) {
                                var e = c.attr("data-swiper-slide-index");
                                if (c.hasClass(d.params.slideDuplicateClass)) {
                                    var t = d.$wrapperEl.children('[data-swiper-slide-index="' + e + '"]:not(.' + d.params.slideDuplicateClass + ")");
                                    d.lazy.loadInSlide(t.index(), !1)
                                } else {
                                    var a = d.$wrapperEl.children("." + d.params.slideDuplicateClass + '[data-swiper-slide-index="' + e + '"]');
                                    d.lazy.loadInSlide(a.index(), !1)
                                }
                            }
                            d.emit("lazyImageReady", c[0], i[0])
                        }
                    }), d.emit("lazyImageLoad", c[0], i[0])
                })
            }
        }, load: function () {
            var i = this, t = i.$wrapperEl, a = i.params, s = i.slides, e = i.activeIndex,
                r = i.virtual && a.virtual.enabled, n = a.lazy, o = a.slidesPerView;

            function l(e) {
                if (r) {
                    if (t.children("." + a.slideClass + '[data-swiper-slide-index="' + e + '"]').length) return !0
                } else if (s[e]) return !0;
                return !1
            }

            function d(e) {
                return r ? L(e).attr("data-swiper-slide-index") : L(e).index()
            }

            if ("auto" === o && (o = 0), i.lazy.initialImageLoaded || (i.lazy.initialImageLoaded = !0), i.params.watchSlidesVisibility) t.children("." + a.slideVisibleClass).each(function (e, t) {
                var a = r ? L(t).attr("data-swiper-slide-index") : L(t).index();
                i.lazy.loadInSlide(a)
            }); else if (1 < o) for (var p = e; p < e + o; p += 1) l(p) && i.lazy.loadInSlide(p); else i.lazy.loadInSlide(e);
            if (n.loadPrevNext) if (1 < o || n.loadPrevNextAmount && 1 < n.loadPrevNextAmount) {
                for (var c = n.loadPrevNextAmount, u = o, h = Math.min(e + u + Math.max(c, u), s.length), v = Math.max(e - Math.max(u, c), 0), f = e + o; f < h; f += 1) l(f) && i.lazy.loadInSlide(f);
                for (var m = v; m < e; m += 1) l(m) && i.lazy.loadInSlide(m)
            } else {
                var g = t.children("." + a.slideNextClass);
                0 < g.length && i.lazy.loadInSlide(d(g));
                var b = t.children("." + a.slidePrevClass);
                0 < b.length && i.lazy.loadInSlide(d(b))
            }
        }
    }, j = {
        LinearSpline: function (e, t) {
            var a, i, s, r, n, o = function (e, t) {
                for (i = -1, a = e.length; 1 < a - i;) e[s = a + i >> 1] <= t ? i = s : a = s;
                return a
            };
            return this.x = e, this.y = t, this.lastIndex = e.length - 1, this.interpolate = function (e) {
                return e ? (n = o(this.x, e), r = n - 1, (e - this.x[r]) * (this.y[n] - this.y[r]) / (this.x[n] - this.x[r]) + this.y[r]) : 0
            }, this
        }, getInterpolateFunction: function (e) {
            var t = this;
            t.controller.spline || (t.controller.spline = t.params.loop ? new j.LinearSpline(t.slidesGrid, e.slidesGrid) : new j.LinearSpline(t.snapGrid, e.snapGrid))
        }, setTranslate: function (e, t) {
            var a, i, s = this, r = s.controller.control;

            function n(e) {
                var t = s.rtlTranslate ? -s.translate : s.translate;
                "slide" === s.params.controller.by && (s.controller.getInterpolateFunction(e), i = -s.controller.spline.interpolate(-t)), i && "container" !== s.params.controller.by || (a = (e.maxTranslate() - e.minTranslate()) / (s.maxTranslate() - s.minTranslate()), i = (t - s.minTranslate()) * a + e.minTranslate()), s.params.controller.inverse && (i = e.maxTranslate() - i), e.updateProgress(i), e.setTranslate(i, s), e.updateActiveIndex(), e.updateSlidesClasses()
            }

            if (Array.isArray(r)) for (var o = 0; o < r.length; o += 1) r[o] !== t && r[o] instanceof S && n(r[o]); else r instanceof S && t !== r && n(r)
        }, setTransition: function (t, e) {
            var a, i = this, s = i.controller.control;

            function r(e) {
                e.setTransition(t, i), 0 !== t && (e.transitionStart(), e.params.autoHeight && V.nextTick(function () {
                    e.updateAutoHeight()
                }), e.$wrapperEl.transitionEnd(function () {
                    s && (e.params.loop && "slide" === i.params.controller.by && e.loopFix(), e.transitionEnd())
                }))
            }

            if (Array.isArray(s)) for (a = 0; a < s.length; a += 1) s[a] !== e && s[a] instanceof S && r(s[a]); else s instanceof S && e !== s && r(s)
        }
    }, U = {
        makeElFocusable: function (e) {
            return e.attr("tabIndex", "0"), e
        }, addElRole: function (e, t) {
            return e.attr("role", t), e
        }, addElLabel: function (e, t) {
            return e.attr("aria-label", t), e
        }, disableEl: function (e) {
            return e.attr("aria-disabled", !0), e
        }, enableEl: function (e) {
            return e.attr("aria-disabled", !1), e
        }, onEnterKey: function (e) {
            var t = this, a = t.params.a11y;
            if (13 === e.keyCode) {
                var i = L(e.target);
                t.navigation && t.navigation.$nextEl && i.is(t.navigation.$nextEl) && (t.isEnd && !t.params.loop || t.slideNext(), t.isEnd ? t.a11y.notify(a.lastSlideMessage) : t.a11y.notify(a.nextSlideMessage)), t.navigation && t.navigation.$prevEl && i.is(t.navigation.$prevEl) && (t.isBeginning && !t.params.loop || t.slidePrev(), t.isBeginning ? t.a11y.notify(a.firstSlideMessage) : t.a11y.notify(a.prevSlideMessage)), t.pagination && i.is("." + t.params.pagination.bulletClass) && i[0].click()
            }
        }, notify: function (e) {
            var t = this.a11y.liveRegion;
            0 !== t.length && (t.html(""), t.html(e))
        }, updateNavigation: function () {
            var e = this;
            if (!e.params.loop) {
                var t = e.navigation, a = t.$nextEl, i = t.$prevEl;
                i && 0 < i.length && (e.isBeginning ? e.a11y.disableEl(i) : e.a11y.enableEl(i)), a && 0 < a.length && (e.isEnd ? e.a11y.disableEl(a) : e.a11y.enableEl(a))
            }
        }, updatePagination: function () {
            var i = this, s = i.params.a11y;
            i.pagination && i.params.pagination.clickable && i.pagination.bullets && i.pagination.bullets.length && i.pagination.bullets.each(function (e, t) {
                var a = L(t);
                i.a11y.makeElFocusable(a), i.a11y.addElRole(a, "button"), i.a11y.addElLabel(a, s.paginationBulletMessage.replace(/{{index}}/, a.index() + 1))
            })
        }, init: function () {
            var e = this;
            e.$el.append(e.a11y.liveRegion);
            var t, a, i = e.params.a11y;
            e.navigation && e.navigation.$nextEl && (t = e.navigation.$nextEl), e.navigation && e.navigation.$prevEl && (a = e.navigation.$prevEl), t && (e.a11y.makeElFocusable(t), e.a11y.addElRole(t, "button"), e.a11y.addElLabel(t, i.nextSlideMessage), t.on("keydown", e.a11y.onEnterKey)), a && (e.a11y.makeElFocusable(a), e.a11y.addElRole(a, "button"), e.a11y.addElLabel(a, i.prevSlideMessage), a.on("keydown", e.a11y.onEnterKey)), e.pagination && e.params.pagination.clickable && e.pagination.bullets && e.pagination.bullets.length && e.pagination.$el.on("keydown", "." + e.params.pagination.bulletClass, e.a11y.onEnterKey)
        }, destroy: function () {
            var e, t, a = this;
            a.a11y.liveRegion && 0 < a.a11y.liveRegion.length && a.a11y.liveRegion.remove(), a.navigation && a.navigation.$nextEl && (e = a.navigation.$nextEl), a.navigation && a.navigation.$prevEl && (t = a.navigation.$prevEl), e && e.off("keydown", a.a11y.onEnterKey), t && t.off("keydown", a.a11y.onEnterKey), a.pagination && a.params.pagination.clickable && a.pagination.bullets && a.pagination.bullets.length && a.pagination.$el.off("keydown", "." + a.params.pagination.bulletClass, a.a11y.onEnterKey)
        }
    }, K = {
        init: function () {
            var e = this;
            if (e.params.history) {
                if (!Y.history || !Y.history.pushState) return e.params.history.enabled = !1, void (e.params.hashNavigation.enabled = !0);
                var t = e.history;
                t.initialized = !0, t.paths = K.getPathValues(), (t.paths.key || t.paths.value) && (t.scrollToSlide(0, t.paths.value, e.params.runCallbacksOnInit), e.params.history.replaceState || Y.addEventListener("popstate", e.history.setHistoryPopState))
            }
        }, destroy: function () {
            this.params.history.replaceState || Y.removeEventListener("popstate", this.history.setHistoryPopState)
        }, setHistoryPopState: function () {
            this.history.paths = K.getPathValues(), this.history.scrollToSlide(this.params.speed, this.history.paths.value, !1)
        }, getPathValues: function () {
            var e = Y.location.pathname.slice(1).split("/").filter(function (e) {
                return "" !== e
            }), t = e.length;
            return {key: e[t - 2], value: e[t - 1]}
        }, setHistory: function (e, t) {
            if (this.history.initialized && this.params.history.enabled) {
                var a = this.slides.eq(t), i = K.slugify(a.attr("data-history"));
                Y.location.pathname.includes(e) || (i = e + "/" + i);
                var s = Y.history.state;
                s && s.value === i || (this.params.history.replaceState ? Y.history.replaceState({value: i}, null, i) : Y.history.pushState({value: i}, null, i))
            }
        }, slugify: function (e) {
            return e.toString().toLowerCase().replace(/\s+/g, "-").replace(/[^\w-]+/g, "").replace(/--+/g, "-").replace(/^-+/, "").replace(/-+$/, "")
        }, scrollToSlide: function (e, t, a) {
            var i = this;
            if (t) for (var s = 0, r = i.slides.length; s < r; s += 1) {
                var n = i.slides.eq(s);
                if (K.slugify(n.attr("data-history")) === t && !n.hasClass(i.params.slideDuplicateClass)) {
                    var o = n.index();
                    i.slideTo(o, e, a)
                }
            } else i.slideTo(0, e, a)
        }
    }, _ = {
        onHashCange: function () {
            var e = this, t = f.location.hash.replace("#", "");
            if (t !== e.slides.eq(e.activeIndex).attr("data-hash")) {
                var a = e.$wrapperEl.children("." + e.params.slideClass + '[data-hash="' + t + '"]').index();
                if (void 0 === a) return;
                e.slideTo(a)
            }
        }, setHash: function () {
            var e = this;
            if (e.hashNavigation.initialized && e.params.hashNavigation.enabled) if (e.params.hashNavigation.replaceState && Y.history && Y.history.replaceState) Y.history.replaceState(null, null, "#" + e.slides.eq(e.activeIndex).attr("data-hash") || ""); else {
                var t = e.slides.eq(e.activeIndex), a = t.attr("data-hash") || t.attr("data-history");
                f.location.hash = a || ""
            }
        }, init: function () {
            var e = this;
            if (!(!e.params.hashNavigation.enabled || e.params.history && e.params.history.enabled)) {
                e.hashNavigation.initialized = !0;
                var t = f.location.hash.replace("#", "");
                if (t) for (var a = 0, i = e.slides.length; a < i; a += 1) {
                    var s = e.slides.eq(a);
                    if ((s.attr("data-hash") || s.attr("data-history")) === t && !s.hasClass(e.params.slideDuplicateClass)) {
                        var r = s.index();
                        e.slideTo(r, 0, e.params.runCallbacksOnInit, !0)
                    }
                }
                e.params.hashNavigation.watchState && L(Y).on("hashchange", e.hashNavigation.onHashCange)
            }
        }, destroy: function () {
            this.params.hashNavigation.watchState && L(Y).off("hashchange", this.hashNavigation.onHashCange)
        }
    }, Z = {
        run: function () {
            var e = this, t = e.slides.eq(e.activeIndex), a = e.params.autoplay.delay;
            t.attr("data-swiper-autoplay") && (a = t.attr("data-swiper-autoplay") || e.params.autoplay.delay), e.autoplay.timeout = V.nextTick(function () {
                e.params.autoplay.reverseDirection ? e.params.loop ? (e.loopFix(), e.slidePrev(e.params.speed, !0, !0), e.emit("autoplay")) : e.isBeginning ? e.params.autoplay.stopOnLastSlide ? e.autoplay.stop() : (e.slideTo(e.slides.length - 1, e.params.speed, !0, !0), e.emit("autoplay")) : (e.slidePrev(e.params.speed, !0, !0), e.emit("autoplay")) : e.params.loop ? (e.loopFix(), e.slideNext(e.params.speed, !0, !0), e.emit("autoplay")) : e.isEnd ? e.params.autoplay.stopOnLastSlide ? e.autoplay.stop() : (e.slideTo(0, e.params.speed, !0, !0), e.emit("autoplay")) : (e.slideNext(e.params.speed, !0, !0), e.emit("autoplay"))
            }, a)
        }, start: function () {
            var e = this;
            return void 0 === e.autoplay.timeout && (!e.autoplay.running && (e.autoplay.running = !0, e.emit("autoplayStart"), e.autoplay.run(), !0))
        }, stop: function () {
            var e = this;
            return !!e.autoplay.running && (void 0 !== e.autoplay.timeout && (e.autoplay.timeout && (clearTimeout(e.autoplay.timeout), e.autoplay.timeout = void 0), e.autoplay.running = !1, e.emit("autoplayStop"), !0))
        }, pause: function (e) {
            var t = this;
            t.autoplay.running && (t.autoplay.paused || (t.autoplay.timeout && clearTimeout(t.autoplay.timeout), t.autoplay.paused = !0, 0 !== e && t.params.autoplay.waitForTransition ? (t.$wrapperEl[0].addEventListener("transitionend", t.autoplay.onTransitionEnd), t.$wrapperEl[0].addEventListener("webkitTransitionEnd", t.autoplay.onTransitionEnd)) : (t.autoplay.paused = !1, t.autoplay.run())))
        }
    }, Q = {
        setTranslate: function () {
            for (var e = this, t = e.slides, a = 0; a < t.length; a += 1) {
                var i = e.slides.eq(a), s = -i[0].swiperSlideOffset;
                e.params.virtualTranslate || (s -= e.translate);
                var r = 0;
                e.isHorizontal() || (r = s, s = 0);
                var n = e.params.fadeEffect.crossFade ? Math.max(1 - Math.abs(i[0].progress), 0) : 1 + Math.min(Math.max(i[0].progress, -1), 0);
                i.css({opacity: n}).transform("translate3d(" + s + "px, " + r + "px, 0px)")
            }
        }, setTransition: function (e) {
            var a = this, t = a.slides, i = a.$wrapperEl;
            if (t.transition(e), a.params.virtualTranslate && 0 !== e) {
                var s = !1;
                t.transitionEnd(function () {
                    if (!s && a && !a.destroyed) {
                        s = !0, a.animating = !1;
                        for (var e = ["webkitTransitionEnd", "transitionend"], t = 0; t < e.length; t += 1) i.trigger(e[t])
                    }
                })
            }
        }
    }, J = {
        setTranslate: function () {
            var e, t = this, a = t.$el, i = t.$wrapperEl, s = t.slides, r = t.width, n = t.height, o = t.rtlTranslate,
                l = t.size, d = t.params.cubeEffect, p = t.isHorizontal(), c = t.virtual && t.params.virtual.enabled,
                u = 0;
            d.shadow && (p ? (0 === (e = i.find(".swiper-cube-shadow")).length && (e = L('<div class="swiper-cube-shadow"></div>'), i.append(e)), e.css({height: r + "px"})) : 0 === (e = a.find(".swiper-cube-shadow")).length && (e = L('<div class="swiper-cube-shadow"></div>'), a.append(e)));
            for (var h = 0; h < s.length; h += 1) {
                var v = s.eq(h), f = h;
                c && (f = parseInt(v.attr("data-swiper-slide-index"), 10));
                var m = 90 * f, g = Math.floor(m / 360);
                o && (m = -m, g = Math.floor(-m / 360));
                var b = Math.max(Math.min(v[0].progress, 1), -1), w = 0, y = 0, x = 0;
                f % 4 == 0 ? (w = 4 * -g * l, x = 0) : (f - 1) % 4 == 0 ? (w = 0, x = 4 * -g * l) : (f - 2) % 4 == 0 ? (w = l + 4 * g * l, x = l) : (f - 3) % 4 == 0 && (w = -l, x = 3 * l + 4 * l * g), o && (w = -w), p || (y = w, w = 0);
                var T = "rotateX(" + (p ? 0 : -m) + "deg) rotateY(" + (p ? m : 0) + "deg) translate3d(" + w + "px, " + y + "px, " + x + "px)";
                if (b <= 1 && -1 < b && (u = 90 * f + 90 * b, o && (u = 90 * -f - 90 * b)), v.transform(T), d.slideShadows) {
                    var E = p ? v.find(".swiper-slide-shadow-left") : v.find(".swiper-slide-shadow-top"),
                        S = p ? v.find(".swiper-slide-shadow-right") : v.find(".swiper-slide-shadow-bottom");
                    0 === E.length && (E = L('<div class="swiper-slide-shadow-' + (p ? "left" : "top") + '"></div>'), v.append(E)), 0 === S.length && (S = L('<div class="swiper-slide-shadow-' + (p ? "right" : "bottom") + '"></div>'), v.append(S)), E.length && (E[0].style.opacity = Math.max(-b, 0)), S.length && (S[0].style.opacity = Math.max(b, 0))
                }
            }
            if (i.css({
                "-webkit-transform-origin": "50% 50% -" + l / 2 + "px",
                "-moz-transform-origin": "50% 50% -" + l / 2 + "px",
                "-ms-transform-origin": "50% 50% -" + l / 2 + "px",
                "transform-origin": "50% 50% -" + l / 2 + "px"
            }), d.shadow) if (p) e.transform("translate3d(0px, " + (r / 2 + d.shadowOffset) + "px, " + -r / 2 + "px) rotateX(90deg) rotateZ(0deg) scale(" + d.shadowScale + ")"); else {
                var C = Math.abs(u) - 90 * Math.floor(Math.abs(u) / 90),
                    M = 1.5 - (Math.sin(2 * C * Math.PI / 360) / 2 + Math.cos(2 * C * Math.PI / 360) / 2),
                    k = d.shadowScale, z = d.shadowScale / M, P = d.shadowOffset;
                e.transform("scale3d(" + k + ", 1, " + z + ") translate3d(0px, " + (n / 2 + P) + "px, " + -n / 2 / z + "px) rotateX(-90deg)")
            }
            var $ = I.isSafari || I.isUiWebView ? -l / 2 : 0;
            i.transform("translate3d(0px,0," + $ + "px) rotateX(" + (t.isHorizontal() ? 0 : u) + "deg) rotateY(" + (t.isHorizontal() ? -u : 0) + "deg)")
        }, setTransition: function (e) {
            var t = this.$el;
            this.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e), this.params.cubeEffect.shadow && !this.isHorizontal() && t.find(".swiper-cube-shadow").transition(e)
        }
    }, ee = {
        setTranslate: function () {
            for (var e = this, t = e.slides, a = e.rtlTranslate, i = 0; i < t.length; i += 1) {
                var s = t.eq(i), r = s[0].progress;
                e.params.flipEffect.limitRotation && (r = Math.max(Math.min(s[0].progress, 1), -1));
                var n = -180 * r, o = 0, l = -s[0].swiperSlideOffset, d = 0;
                if (e.isHorizontal() ? a && (n = -n) : (d = l, o = -n, n = l = 0), s[0].style.zIndex = -Math.abs(Math.round(r)) + t.length, e.params.flipEffect.slideShadows) {
                    var p = e.isHorizontal() ? s.find(".swiper-slide-shadow-left") : s.find(".swiper-slide-shadow-top"),
                        c = e.isHorizontal() ? s.find(".swiper-slide-shadow-right") : s.find(".swiper-slide-shadow-bottom");
                    0 === p.length && (p = L('<div class="swiper-slide-shadow-' + (e.isHorizontal() ? "left" : "top") + '"></div>'), s.append(p)), 0 === c.length && (c = L('<div class="swiper-slide-shadow-' + (e.isHorizontal() ? "right" : "bottom") + '"></div>'), s.append(c)), p.length && (p[0].style.opacity = Math.max(-r, 0)), c.length && (c[0].style.opacity = Math.max(r, 0))
                }
                s.transform("translate3d(" + l + "px, " + d + "px, 0px) rotateX(" + o + "deg) rotateY(" + n + "deg)")
            }
        }, setTransition: function (e) {
            var a = this, t = a.slides, i = a.activeIndex, s = a.$wrapperEl;
            if (t.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e), a.params.virtualTranslate && 0 !== e) {
                var r = !1;
                t.eq(i).transitionEnd(function () {
                    if (!r && a && !a.destroyed) {
                        r = !0, a.animating = !1;
                        for (var e = ["webkitTransitionEnd", "transitionend"], t = 0; t < e.length; t += 1) s.trigger(e[t])
                    }
                })
            }
        }
    }, te = {
        setTranslate: function () {
            for (var e = this, t = e.width, a = e.height, i = e.slides, s = e.$wrapperEl, r = e.slidesSizesGrid, n = e.params.coverflowEffect, o = e.isHorizontal(), l = e.translate, d = o ? t / 2 - l : a / 2 - l, p = o ? n.rotate : -n.rotate, c = n.depth, u = 0, h = i.length; u < h; u += 1) {
                var v = i.eq(u), f = r[u], m = (d - v[0].swiperSlideOffset - f / 2) / f * n.modifier, g = o ? p * m : 0,
                    b = o ? 0 : p * m, w = -c * Math.abs(m), y = o ? 0 : n.stretch * m, x = o ? n.stretch * m : 0;
                Math.abs(x) < .001 && (x = 0), Math.abs(y) < .001 && (y = 0), Math.abs(w) < .001 && (w = 0), Math.abs(g) < .001 && (g = 0), Math.abs(b) < .001 && (b = 0);
                var T = "translate3d(" + x + "px," + y + "px," + w + "px)  rotateX(" + b + "deg) rotateY(" + g + "deg)";
                if (v.transform(T), v[0].style.zIndex = 1 - Math.abs(Math.round(m)), n.slideShadows) {
                    var E = o ? v.find(".swiper-slide-shadow-left") : v.find(".swiper-slide-shadow-top"),
                        S = o ? v.find(".swiper-slide-shadow-right") : v.find(".swiper-slide-shadow-bottom");
                    0 === E.length && (E = L('<div class="swiper-slide-shadow-' + (o ? "left" : "top") + '"></div>'), v.append(E)), 0 === S.length && (S = L('<div class="swiper-slide-shadow-' + (o ? "right" : "bottom") + '"></div>'), v.append(S)), E.length && (E[0].style.opacity = 0 < m ? m : 0), S.length && (S[0].style.opacity = 0 < -m ? -m : 0)
                }
            }
            (R.pointerEvents || R.prefixedPointerEvents) && (s[0].style.perspectiveOrigin = d + "px 50%")
        }, setTransition: function (e) {
            this.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e)
        }
    }, ae = {
        init: function () {
            var e = this, t = e.params.thumbs, a = e.constructor;
            t.swiper instanceof a ? (e.thumbs.swiper = t.swiper, V.extend(e.thumbs.swiper.originalParams, {
                watchSlidesProgress: !0,
                slideToClickedSlide: !1
            }), V.extend(e.thumbs.swiper.params, {
                watchSlidesProgress: !0,
                slideToClickedSlide: !1
            })) : V.isObject(t.swiper) && (e.thumbs.swiper = new a(V.extend({}, t.swiper, {
                watchSlidesVisibility: !0,
                watchSlidesProgress: !0,
                slideToClickedSlide: !1
            })), e.thumbs.swiperCreated = !0), e.thumbs.swiper.$el.addClass(e.params.thumbs.thumbsContainerClass), e.thumbs.swiper.on("tap", e.thumbs.onThumbClick)
        }, onThumbClick: function () {
            var e = this, t = e.thumbs.swiper;
            if (t) {
                var a = t.clickedIndex;
                if (null != a) {
                    var i;
                    if (i = t.params.loop ? parseInt(L(t.clickedSlide).attr("data-swiper-slide-index"), 10) : a, e.params.loop) {
                        var s = e.activeIndex;
                        e.slides.eq(s).hasClass(e.params.slideDuplicateClass) && (e.loopFix(), e._clientLeft = e.$wrapperEl[0].clientLeft, s = e.activeIndex);
                        var r = e.slides.eq(s).prevAll('[data-swiper-slide-index="' + i + '"]').eq(0).index(),
                            n = e.slides.eq(s).nextAll('[data-swiper-slide-index="' + i + '"]').eq(0).index();
                        i = void 0 === r ? n : void 0 === n ? r : n - s < s - r ? n : r
                    }
                    e.slideTo(i)
                }
            }
        }, update: function (e) {
            var t = this, a = t.thumbs.swiper;
            if (a) {
                var i = "auto" === a.params.slidesPerView ? a.slidesPerViewDynamic() : a.params.slidesPerView;
                if (t.realIndex !== a.realIndex) {
                    var s, r = a.activeIndex;
                    if (a.params.loop) {
                        a.slides.eq(r).hasClass(a.params.slideDuplicateClass) && (a.loopFix(), a._clientLeft = a.$wrapperEl[0].clientLeft, r = a.activeIndex);
                        var n = a.slides.eq(r).prevAll('[data-swiper-slide-index="' + t.realIndex + '"]').eq(0).index(),
                            o = a.slides.eq(r).nextAll('[data-swiper-slide-index="' + t.realIndex + '"]').eq(0).index();
                        s = void 0 === n ? o : void 0 === o ? n : o - r < r - n ? o : n
                    } else s = t.realIndex;
                    a.visibleSlidesIndexes.indexOf(s) < 0 && (a.params.centeredSlides ? s = r < s ? s - Math.floor(i / 2) + 1 : s + Math.floor(i / 2) - 1 : r < s && (s = s - i + 1), a.slideTo(s, e ? 0 : void 0))
                }
                var l = 1, d = t.params.thumbs.slideThumbActiveClass;
                if (1 < t.params.slidesPerView && !t.params.centeredSlides && (l = t.params.slidesPerView), a.slides.removeClass(d), a.params.loop) for (var p = 0; p < l; p += 1) a.$wrapperEl.children('[data-swiper-slide-index="' + (t.realIndex + p) + '"]').addClass(d); else for (var c = 0; c < l; c += 1) a.slides.eq(t.realIndex + c).addClass(d)
            }
        }
    }, ie = [C, M, k, z, $, O, H, {
        name: "mousewheel",
        params: {
            mousewheel: {
                enabled: !1,
                releaseOnEdges: !1,
                invert: !1,
                forceToAxis: !1,
                sensitivity: 1,
                eventsTarged: "container"
            }
        },
        create: function () {
            var e = this;
            V.extend(e, {
                mousewheel: {
                    enabled: !1,
                    enable: B.enable.bind(e),
                    disable: B.disable.bind(e),
                    handle: B.handle.bind(e),
                    handleMouseEnter: B.handleMouseEnter.bind(e),
                    handleMouseLeave: B.handleMouseLeave.bind(e),
                    lastScrollTime: V.now()
                }
            })
        },
        on: {
            init: function () {
                this.params.mousewheel.enabled && this.mousewheel.enable()
            }, destroy: function () {
                this.mousewheel.enabled && this.mousewheel.disable()
            }
        }
    }, {
        name: "navigation",
        params: {
            navigation: {
                nextEl: null,
                prevEl: null,
                hideOnClick: !1,
                disabledClass: "swiper-button-disabled",
                hiddenClass: "swiper-button-hidden",
                lockClass: "swiper-button-lock"
            }
        },
        create: function () {
            V.extend(this, {
                navigation: {
                    init: G.init.bind(this),
                    update: G.update.bind(this),
                    destroy: G.destroy.bind(this)
                }
            })
        },
        on: {
            init: function () {
                this.navigation.init(), this.navigation.update()
            }, toEdge: function () {
                this.navigation.update()
            }, fromEdge: function () {
                this.navigation.update()
            }, destroy: function () {
                this.navigation.destroy()
            }, click: function (e) {
                var t = this.navigation, a = t.$nextEl, i = t.$prevEl;
                !this.params.navigation.hideOnClick || L(e.target).is(i) || L(e.target).is(a) || (a && a.toggleClass(this.params.navigation.hiddenClass), i && i.toggleClass(this.params.navigation.hiddenClass))
            }
        }
    }, {
        name: "pagination",
        params: {
            pagination: {
                el: null,
                bulletElement: "span",
                clickable: !1,
                hideOnClick: !1,
                renderBullet: null,
                renderProgressbar: null,
                renderFraction: null,
                renderCustom: null,
                progressbarOpposite: !1,
                type: "bullets",
                dynamicBullets: !1,
                dynamicMainBullets: 1,
                formatFractionCurrent: function (e) {
                    return e
                },
                formatFractionTotal: function (e) {
                    return e
                },
                bulletClass: "swiper-pagination-bullet",
                bulletActiveClass: "swiper-pagination-bullet-active",
                modifierClass: "swiper-pagination-",
                currentClass: "swiper-pagination-current",
                totalClass: "swiper-pagination-total",
                hiddenClass: "swiper-pagination-hidden",
                progressbarFillClass: "swiper-pagination-progressbar-fill",
                progressbarOppositeClass: "swiper-pagination-progressbar-opposite",
                clickableClass: "swiper-pagination-clickable",
                lockClass: "swiper-pagination-lock"
            }
        },
        create: function () {
            var e = this;
            V.extend(e, {
                pagination: {
                    init: N.init.bind(e),
                    render: N.render.bind(e),
                    update: N.update.bind(e),
                    destroy: N.destroy.bind(e),
                    dynamicBulletIndex: 0
                }
            })
        },
        on: {
            init: function () {
                this.pagination.init(), this.pagination.render(), this.pagination.update()
            }, activeIndexChange: function () {
                this.params.loop ? this.pagination.update() : void 0 === this.snapIndex && this.pagination.update()
            }, snapIndexChange: function () {
                this.params.loop || this.pagination.update()
            }, slidesLengthChange: function () {
                this.params.loop && (this.pagination.render(), this.pagination.update())
            }, snapGridLengthChange: function () {
                this.params.loop || (this.pagination.render(), this.pagination.update())
            }, destroy: function () {
                this.pagination.destroy()
            }, click: function (e) {
                var t = this;
                t.params.pagination.el && t.params.pagination.hideOnClick && 0 < t.pagination.$el.length && !L(e.target).hasClass(t.params.pagination.bulletClass) && t.pagination.$el.toggleClass(t.params.pagination.hiddenClass)
            }
        }
    }, {
        name: "scrollbar",
        params: {
            scrollbar: {
                el: null,
                dragSize: "auto",
                hide: !1,
                draggable: !1,
                snapOnRelease: !0,
                lockClass: "swiper-scrollbar-lock",
                dragClass: "swiper-scrollbar-drag"
            }
        },
        create: function () {
            var e = this;
            V.extend(e, {
                scrollbar: {
                    init: X.init.bind(e),
                    destroy: X.destroy.bind(e),
                    updateSize: X.updateSize.bind(e),
                    setTranslate: X.setTranslate.bind(e),
                    setTransition: X.setTransition.bind(e),
                    enableDraggable: X.enableDraggable.bind(e),
                    disableDraggable: X.disableDraggable.bind(e),
                    setDragPosition: X.setDragPosition.bind(e),
                    onDragStart: X.onDragStart.bind(e),
                    onDragMove: X.onDragMove.bind(e),
                    onDragEnd: X.onDragEnd.bind(e),
                    isTouched: !1,
                    timeout: null,
                    dragTimeout: null
                }
            })
        },
        on: {
            init: function () {
                this.scrollbar.init(), this.scrollbar.updateSize(), this.scrollbar.setTranslate()
            }, update: function () {
                this.scrollbar.updateSize()
            }, resize: function () {
                this.scrollbar.updateSize()
            }, observerUpdate: function () {
                this.scrollbar.updateSize()
            }, setTranslate: function () {
                this.scrollbar.setTranslate()
            }, setTransition: function (e) {
                this.scrollbar.setTransition(e)
            }, destroy: function () {
                this.scrollbar.destroy()
            }
        }
    }, {
        name: "parallax", params: {parallax: {enabled: !1}}, create: function () {
            V.extend(this, {
                parallax: {
                    setTransform: F.setTransform.bind(this),
                    setTranslate: F.setTranslate.bind(this),
                    setTransition: F.setTransition.bind(this)
                }
            })
        }, on: {
            beforeInit: function () {
                this.params.parallax.enabled && (this.params.watchSlidesProgress = !0, this.originalParams.watchSlidesProgress = !0)
            }, init: function () {
                this.params.parallax && this.parallax.setTranslate()
            }, setTranslate: function () {
                this.params.parallax && this.parallax.setTranslate()
            }, setTransition: function (e) {
                this.params.parallax && this.parallax.setTransition(e)
            }
        }
    }, {
        name: "zoom",
        params: {
            zoom: {
                enabled: !1,
                maxRatio: 3,
                minRatio: 1,
                toggle: !0,
                containerClass: "swiper-zoom-container",
                zoomedSlideClass: "swiper-slide-zoomed"
            }
        },
        create: function () {
            var t = this, a = {
                enabled: !1,
                scale: 1,
                currentScale: 1,
                isScaling: !1,
                gesture: {
                    $slideEl: void 0,
                    slideWidth: void 0,
                    slideHeight: void 0,
                    $imageEl: void 0,
                    $imageWrapEl: void 0,
                    maxRatio: 3
                },
                image: {
                    isTouched: void 0,
                    isMoved: void 0,
                    currentX: void 0,
                    currentY: void 0,
                    minX: void 0,
                    minY: void 0,
                    maxX: void 0,
                    maxY: void 0,
                    width: void 0,
                    height: void 0,
                    startX: void 0,
                    startY: void 0,
                    touchesStart: {},
                    touchesCurrent: {}
                },
                velocity: {x: void 0, y: void 0, prevPositionX: void 0, prevPositionY: void 0, prevTime: void 0}
            };
            "onGestureStart onGestureChange onGestureEnd onTouchStart onTouchMove onTouchEnd onTransitionEnd toggle enable disable in out".split(" ").forEach(function (e) {
                a[e] = q[e].bind(t)
            }), V.extend(t, {zoom: a})
        },
        on: {
            init: function () {
                this.params.zoom.enabled && this.zoom.enable()
            }, destroy: function () {
                this.zoom.disable()
            }, touchStart: function (e) {
                this.zoom.enabled && this.zoom.onTouchStart(e)
            }, touchEnd: function (e) {
                this.zoom.enabled && this.zoom.onTouchEnd(e)
            }, doubleTap: function (e) {
                this.params.zoom.enabled && this.zoom.enabled && this.params.zoom.toggle && this.zoom.toggle(e)
            }, transitionEnd: function () {
                this.zoom.enabled && this.params.zoom.enabled && this.zoom.onTransitionEnd()
            }
        }
    }, {
        name: "lazy",
        params: {
            lazy: {
                enabled: !1,
                loadPrevNext: !1,
                loadPrevNextAmount: 1,
                loadOnTransitionStart: !1,
                elementClass: "swiper-lazy",
                loadingClass: "swiper-lazy-loading",
                loadedClass: "swiper-lazy-loaded",
                preloaderClass: "swiper-lazy-preloader"
            }
        },
        create: function () {
            V.extend(this, {
                lazy: {
                    initialImageLoaded: !1,
                    load: W.load.bind(this),
                    loadInSlide: W.loadInSlide.bind(this)
                }
            })
        },
        on: {
            beforeInit: function () {
                this.params.lazy.enabled && this.params.preloadImages && (this.params.preloadImages = !1)
            }, init: function () {
                this.params.lazy.enabled && !this.params.loop && 0 === this.params.initialSlide && this.lazy.load()
            }, scroll: function () {
                this.params.freeMode && !this.params.freeModeSticky && this.lazy.load()
            }, resize: function () {
                this.params.lazy.enabled && this.lazy.load()
            }, scrollbarDragMove: function () {
                this.params.lazy.enabled && this.lazy.load()
            }, transitionStart: function () {
                var e = this;
                e.params.lazy.enabled && (e.params.lazy.loadOnTransitionStart || !e.params.lazy.loadOnTransitionStart && !e.lazy.initialImageLoaded) && e.lazy.load()
            }, transitionEnd: function () {
                this.params.lazy.enabled && !this.params.lazy.loadOnTransitionStart && this.lazy.load()
            }
        }
    }, {
        name: "controller", params: {controller: {control: void 0, inverse: !1, by: "slide"}}, create: function () {
            var e = this;
            V.extend(e, {
                controller: {
                    control: e.params.controller.control,
                    getInterpolateFunction: j.getInterpolateFunction.bind(e),
                    setTranslate: j.setTranslate.bind(e),
                    setTransition: j.setTransition.bind(e)
                }
            })
        }, on: {
            update: function () {
                this.controller.control && this.controller.spline && (this.controller.spline = void 0, delete this.controller.spline)
            }, resize: function () {
                this.controller.control && this.controller.spline && (this.controller.spline = void 0, delete this.controller.spline)
            }, observerUpdate: function () {
                this.controller.control && this.controller.spline && (this.controller.spline = void 0, delete this.controller.spline)
            }, setTranslate: function (e, t) {
                this.controller.control && this.controller.setTranslate(e, t)
            }, setTransition: function (e, t) {
                this.controller.control && this.controller.setTransition(e, t)
            }
        }
    }, {
        name: "a11y",
        params: {
            a11y: {
                enabled: !0,
                notificationClass: "swiper-notification",
                prevSlideMessage: "Previous slide",
                nextSlideMessage: "Next slide",
                firstSlideMessage: "This is the first slide",
                lastSlideMessage: "This is the last slide",
                paginationBulletMessage: "Go to slide {{index}}"
            }
        },
        create: function () {
            var t = this;
            V.extend(t, {a11y: {liveRegion: L('<span class="' + t.params.a11y.notificationClass + '" aria-live="assertive" aria-atomic="true"></span>')}}), Object.keys(U).forEach(function (e) {
                t.a11y[e] = U[e].bind(t)
            })
        },
        on: {
            init: function () {
                this.params.a11y.enabled && (this.a11y.init(), this.a11y.updateNavigation())
            }, toEdge: function () {
                this.params.a11y.enabled && this.a11y.updateNavigation()
            }, fromEdge: function () {
                this.params.a11y.enabled && this.a11y.updateNavigation()
            }, paginationUpdate: function () {
                this.params.a11y.enabled && this.a11y.updatePagination()
            }, destroy: function () {
                this.params.a11y.enabled && this.a11y.destroy()
            }
        }
    }, {
        name: "history", params: {history: {enabled: !1, replaceState: !1, key: "slides"}}, create: function () {
            var e = this;
            V.extend(e, {
                history: {
                    init: K.init.bind(e),
                    setHistory: K.setHistory.bind(e),
                    setHistoryPopState: K.setHistoryPopState.bind(e),
                    scrollToSlide: K.scrollToSlide.bind(e),
                    destroy: K.destroy.bind(e)
                }
            })
        }, on: {
            init: function () {
                this.params.history.enabled && this.history.init()
            }, destroy: function () {
                this.params.history.enabled && this.history.destroy()
            }, transitionEnd: function () {
                this.history.initialized && this.history.setHistory(this.params.history.key, this.activeIndex)
            }
        }
    }, {
        name: "hash-navigation",
        params: {hashNavigation: {enabled: !1, replaceState: !1, watchState: !1}},
        create: function () {
            var e = this;
            V.extend(e, {
                hashNavigation: {
                    initialized: !1,
                    init: _.init.bind(e),
                    destroy: _.destroy.bind(e),
                    setHash: _.setHash.bind(e),
                    onHashCange: _.onHashCange.bind(e)
                }
            })
        },
        on: {
            init: function () {
                this.params.hashNavigation.enabled && this.hashNavigation.init()
            }, destroy: function () {
                this.params.hashNavigation.enabled && this.hashNavigation.destroy()
            }, transitionEnd: function () {
                this.hashNavigation.initialized && this.hashNavigation.setHash()
            }
        }
    }, {
        name: "autoplay",
        params: {
            autoplay: {
                enabled: !1,
                delay: 3e3,
                waitForTransition: !0,
                disableOnInteraction: !0,
                stopOnLastSlide: !1,
                reverseDirection: !1
            }
        },
        create: function () {
            var t = this;
            V.extend(t, {
                autoplay: {
                    running: !1,
                    paused: !1,
                    run: Z.run.bind(t),
                    start: Z.start.bind(t),
                    stop: Z.stop.bind(t),
                    pause: Z.pause.bind(t),
                    onTransitionEnd: function (e) {
                        t && !t.destroyed && t.$wrapperEl && e.target === this && (t.$wrapperEl[0].removeEventListener("transitionend", t.autoplay.onTransitionEnd), t.$wrapperEl[0].removeEventListener("webkitTransitionEnd", t.autoplay.onTransitionEnd), t.autoplay.paused = !1, t.autoplay.running ? t.autoplay.run() : t.autoplay.stop())
                    }
                }
            })
        },
        on: {
            init: function () {
                this.params.autoplay.enabled && this.autoplay.start()
            }, beforeTransitionStart: function (e, t) {
                this.autoplay.running && (t || !this.params.autoplay.disableOnInteraction ? this.autoplay.pause(e) : this.autoplay.stop())
            }, sliderFirstMove: function () {
                this.autoplay.running && (this.params.autoplay.disableOnInteraction ? this.autoplay.stop() : this.autoplay.pause())
            }, destroy: function () {
                this.autoplay.running && this.autoplay.stop()
            }
        }
    }, {
        name: "effect-fade", params: {fadeEffect: {crossFade: !1}}, create: function () {
            V.extend(this, {
                fadeEffect: {
                    setTranslate: Q.setTranslate.bind(this),
                    setTransition: Q.setTransition.bind(this)
                }
            })
        }, on: {
            beforeInit: function () {
                var e = this;
                if ("fade" === e.params.effect) {
                    e.classNames.push(e.params.containerModifierClass + "fade");
                    var t = {
                        slidesPerView: 1,
                        slidesPerColumn: 1,
                        slidesPerGroup: 1,
                        watchSlidesProgress: !0,
                        spaceBetween: 0,
                        virtualTranslate: !0
                    };
                    V.extend(e.params, t), V.extend(e.originalParams, t)
                }
            }, setTranslate: function () {
                "fade" === this.params.effect && this.fadeEffect.setTranslate()
            }, setTransition: function (e) {
                "fade" === this.params.effect && this.fadeEffect.setTransition(e)
            }
        }
    }, {
        name: "effect-cube",
        params: {cubeEffect: {slideShadows: !0, shadow: !0, shadowOffset: 20, shadowScale: .94}},
        create: function () {
            V.extend(this, {
                cubeEffect: {
                    setTranslate: J.setTranslate.bind(this),
                    setTransition: J.setTransition.bind(this)
                }
            })
        },
        on: {
            beforeInit: function () {
                var e = this;
                if ("cube" === e.params.effect) {
                    e.classNames.push(e.params.containerModifierClass + "cube"), e.classNames.push(e.params.containerModifierClass + "3d");
                    var t = {
                        slidesPerView: 1,
                        slidesPerColumn: 1,
                        slidesPerGroup: 1,
                        watchSlidesProgress: !0,
                        resistanceRatio: 0,
                        spaceBetween: 0,
                        centeredSlides: !1,
                        virtualTranslate: !0
                    };
                    V.extend(e.params, t), V.extend(e.originalParams, t)
                }
            }, setTranslate: function () {
                "cube" === this.params.effect && this.cubeEffect.setTranslate()
            }, setTransition: function (e) {
                "cube" === this.params.effect && this.cubeEffect.setTransition(e)
            }
        }
    }, {
        name: "effect-flip", params: {flipEffect: {slideShadows: !0, limitRotation: !0}}, create: function () {
            V.extend(this, {
                flipEffect: {
                    setTranslate: ee.setTranslate.bind(this),
                    setTransition: ee.setTransition.bind(this)
                }
            })
        }, on: {
            beforeInit: function () {
                var e = this;
                if ("flip" === e.params.effect) {
                    e.classNames.push(e.params.containerModifierClass + "flip"), e.classNames.push(e.params.containerModifierClass + "3d");
                    var t = {
                        slidesPerView: 1,
                        slidesPerColumn: 1,
                        slidesPerGroup: 1,
                        watchSlidesProgress: !0,
                        spaceBetween: 0,
                        virtualTranslate: !0
                    };
                    V.extend(e.params, t), V.extend(e.originalParams, t)
                }
            }, setTranslate: function () {
                "flip" === this.params.effect && this.flipEffect.setTranslate()
            }, setTransition: function (e) {
                "flip" === this.params.effect && this.flipEffect.setTransition(e)
            }
        }
    }, {
        name: "effect-coverflow",
        params: {coverflowEffect: {rotate: 50, stretch: 0, depth: 100, modifier: 1, slideShadows: !0}},
        create: function () {
            V.extend(this, {
                coverflowEffect: {
                    setTranslate: te.setTranslate.bind(this),
                    setTransition: te.setTransition.bind(this)
                }
            })
        },
        on: {
            beforeInit: function () {
                var e = this;
                "coverflow" === e.params.effect && (e.classNames.push(e.params.containerModifierClass + "coverflow"), e.classNames.push(e.params.containerModifierClass + "3d"), e.params.watchSlidesProgress = !0, e.originalParams.watchSlidesProgress = !0)
            }, setTranslate: function () {
                "coverflow" === this.params.effect && this.coverflowEffect.setTranslate()
            }, setTransition: function (e) {
                "coverflow" === this.params.effect && this.coverflowEffect.setTransition(e)
            }
        }
    }, {
        name: "thumbs",
        params: {
            thumbs: {
                swiper: null,
                slideThumbActiveClass: "swiper-slide-thumb-active",
                thumbsContainerClass: "swiper-container-thumbs"
            }
        },
        create: function () {
            V.extend(this, {
                thumbs: {
                    swiper: null,
                    init: ae.init.bind(this),
                    update: ae.update.bind(this),
                    onThumbClick: ae.onThumbClick.bind(this)
                }
            })
        },
        on: {
            beforeInit: function () {
                var e = this.params.thumbs;
                e && e.swiper && (this.thumbs.init(), this.thumbs.update(!0))
            }, slideChange: function () {
                this.thumbs.swiper && this.thumbs.update()
            }, update: function () {
                this.thumbs.swiper && this.thumbs.update()
            }, resize: function () {
                this.thumbs.swiper && this.thumbs.update()
            }, observerUpdate: function () {
                this.thumbs.swiper && this.thumbs.update()
            }, setTransition: function (e) {
                var t = this.thumbs.swiper;
                t && t.setTransition(e)
            }, beforeDestroy: function () {
                var e = this.thumbs.swiper;
                e && this.thumbs.swiperCreated && e && e.destroy()
            }
        }
    }];
    return void 0 === S.use && (S.use = S.Class.use, S.installModule = S.Class.installModule), S.use(ie), S
});
//# sourceMappingURL=swiper.min.js.map

/************
 https://michalsnik.github.io/aos/ AOS 애니메이션
 ************/
!function (e, t) {
    "object" == typeof exports && "object" == typeof module ? module.exports = t() : "function" == typeof define && define.amd ? define([], t) : "object" == typeof exports ? exports.AOS = t() : e.AOS = t()
}(this, function () {
    return function (e) {
        function t(o) {
            if (n[o]) return n[o].exports;
            var i = n[o] = {exports: {}, id: o, loaded: !1};
            return e[o].call(i.exports, i, i.exports, t), i.loaded = !0, i.exports
        }

        var n = {};
        return t.m = e, t.c = n, t.p = "dist/", t(0)
    }([function (e, t, n) {
        "use strict";

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        var i = Object.assign || function (e) {
                for (var t = 1; t < arguments.length; t++) {
                    var n = arguments[t];
                    for (var o in n) Object.prototype.hasOwnProperty.call(n, o) && (e[o] = n[o])
                }
                return e
            }, r = n(1), a = (o(r), n(6)), u = o(a), c = n(7), f = o(c), s = n(8), d = o(s), l = n(9), p = o(l), m = n(10),
            b = o(m), v = n(11), y = o(v), g = n(14), h = o(g), w = [], k = !1, x = {
                offset: 120,
                delay: 0,
                easing: "ease",
                duration: 400,
                disable: !1,
                once: !1,
                startEvent: "DOMContentLoaded",
                throttleDelay: 99,
                debounceDelay: 50,
                disableMutationObserver: !1
            }, j = function () {
                var e = arguments.length > 0 && void 0 !== arguments[0] && arguments[0];
                if (e && (k = !0), k) return w = (0, y.default)(w, x), (0, b.default)(w, x.once), w
            }, O = function () {
                w = (0, h.default)(), j()
            }, _ = function () {
                w.forEach(function (e, t) {
                    e.node.removeAttribute("data-aos"), e.node.removeAttribute("data-aos-easing"), e.node.removeAttribute("data-aos-duration"), e.node.removeAttribute("data-aos-delay")
                })
            }, S = function (e) {
                return e === !0 || "mobile" === e && p.default.mobile() || "phone" === e && p.default.phone() || "tablet" === e && p.default.tablet() || "function" == typeof e && e() === !0
            }, z = function (e) {
                x = i(x, e), w = (0, h.default)();
                var t = document.all && !window.atob;
                return S(x.disable) || t ? _() : (document.querySelector("body").setAttribute("data-aos-easing", x.easing), document.querySelector("body").setAttribute("data-aos-duration", x.duration), document.querySelector("body").setAttribute("data-aos-delay", x.delay), "DOMContentLoaded" === x.startEvent && ["complete", "interactive"].indexOf(document.readyState) > -1 ? j(!0) : "load" === x.startEvent ? window.addEventListener(x.startEvent, function () {
                    j(!0)
                }) : document.addEventListener(x.startEvent, function () {
                    j(!0)
                }), window.addEventListener("resize", (0, f.default)(j, x.debounceDelay, !0)), window.addEventListener("orientationchange", (0, f.default)(j, x.debounceDelay, !0)), window.addEventListener("scroll", (0, u.default)(function () {
                    (0, b.default)(w, x.once)
                }, x.throttleDelay)), x.disableMutationObserver || (0, d.default)("[data-aos]", O), w)
            };
        e.exports = {init: z, refresh: j, refreshHard: O}
    }, function (e, t) {
    }, , , , , function (e, t) {
        (function (t) {
            "use strict";

            function n(e, t, n) {
                function o(t) {
                    var n = b, o = v;
                    return b = v = void 0, k = t, g = e.apply(o, n)
                }

                function r(e) {
                    return k = e, h = setTimeout(s, t), _ ? o(e) : g
                }

                function a(e) {
                    var n = e - w, o = e - k, i = t - n;
                    return S ? j(i, y - o) : i
                }

                function c(e) {
                    var n = e - w, o = e - k;
                    return void 0 === w || n >= t || n < 0 || S && o >= y
                }

                function s() {
                    var e = O();
                    return c(e) ? d(e) : void (h = setTimeout(s, a(e)))
                }

                function d(e) {
                    return h = void 0, z && b ? o(e) : (b = v = void 0, g)
                }

                function l() {
                    void 0 !== h && clearTimeout(h), k = 0, b = w = v = h = void 0
                }

                function p() {
                    return void 0 === h ? g : d(O())
                }

                function m() {
                    var e = O(), n = c(e);
                    if (b = arguments, v = this, w = e, n) {
                        if (void 0 === h) return r(w);
                        if (S) return h = setTimeout(s, t), o(w)
                    }
                    return void 0 === h && (h = setTimeout(s, t)), g
                }

                var b, v, y, g, h, w, k = 0, _ = !1, S = !1, z = !0;
                if ("function" != typeof e) throw new TypeError(f);
                return t = u(t) || 0, i(n) && (_ = !!n.leading, S = "maxWait" in n, y = S ? x(u(n.maxWait) || 0, t) : y, z = "trailing" in n ? !!n.trailing : z), m.cancel = l, m.flush = p, m
            }

            function o(e, t, o) {
                var r = !0, a = !0;
                if ("function" != typeof e) throw new TypeError(f);
                return i(o) && (r = "leading" in o ? !!o.leading : r, a = "trailing" in o ? !!o.trailing : a), n(e, t, {
                    leading: r,
                    maxWait: t,
                    trailing: a
                })
            }

            function i(e) {
                var t = "undefined" == typeof e ? "undefined" : c(e);
                return !!e && ("object" == t || "function" == t)
            }

            function r(e) {
                return !!e && "object" == ("undefined" == typeof e ? "undefined" : c(e))
            }

            function a(e) {
                return "symbol" == ("undefined" == typeof e ? "undefined" : c(e)) || r(e) && k.call(e) == d
            }

            function u(e) {
                if ("number" == typeof e) return e;
                if (a(e)) return s;
                if (i(e)) {
                    var t = "function" == typeof e.valueOf ? e.valueOf() : e;
                    e = i(t) ? t + "" : t
                }
                if ("string" != typeof e) return 0 === e ? e : +e;
                e = e.replace(l, "");
                var n = m.test(e);
                return n || b.test(e) ? v(e.slice(2), n ? 2 : 8) : p.test(e) ? s : +e
            }

            var c = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
                    return typeof e
                } : function (e) {
                    return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
                }, f = "Expected a function", s = NaN, d = "[object Symbol]", l = /^\s+|\s+$/g, p = /^[-+]0x[0-9a-f]+$/i,
                m = /^0b[01]+$/i, b = /^0o[0-7]+$/i, v = parseInt,
                y = "object" == ("undefined" == typeof t ? "undefined" : c(t)) && t && t.Object === Object && t,
                g = "object" == ("undefined" == typeof self ? "undefined" : c(self)) && self && self.Object === Object && self,
                h = y || g || Function("return this")(), w = Object.prototype, k = w.toString, x = Math.max,
                j = Math.min, O = function () {
                    return h.Date.now()
                };
            e.exports = o
        }).call(t, function () {
            return this
        }())
    }, function (e, t) {
        (function (t) {
            "use strict";

            function n(e, t, n) {
                function i(t) {
                    var n = b, o = v;
                    return b = v = void 0, O = t, g = e.apply(o, n)
                }

                function r(e) {
                    return O = e, h = setTimeout(s, t), _ ? i(e) : g
                }

                function u(e) {
                    var n = e - w, o = e - O, i = t - n;
                    return S ? x(i, y - o) : i
                }

                function f(e) {
                    var n = e - w, o = e - O;
                    return void 0 === w || n >= t || n < 0 || S && o >= y
                }

                function s() {
                    var e = j();
                    return f(e) ? d(e) : void (h = setTimeout(s, u(e)))
                }

                function d(e) {
                    return h = void 0, z && b ? i(e) : (b = v = void 0, g)
                }

                function l() {
                    void 0 !== h && clearTimeout(h), O = 0, b = w = v = h = void 0
                }

                function p() {
                    return void 0 === h ? g : d(j())
                }

                function m() {
                    var e = j(), n = f(e);
                    if (b = arguments, v = this, w = e, n) {
                        if (void 0 === h) return r(w);
                        if (S) return h = setTimeout(s, t), i(w)
                    }
                    return void 0 === h && (h = setTimeout(s, t)), g
                }

                var b, v, y, g, h, w, O = 0, _ = !1, S = !1, z = !0;
                if ("function" != typeof e) throw new TypeError(c);
                return t = a(t) || 0, o(n) && (_ = !!n.leading, S = "maxWait" in n, y = S ? k(a(n.maxWait) || 0, t) : y, z = "trailing" in n ? !!n.trailing : z), m.cancel = l, m.flush = p, m
            }

            function o(e) {
                var t = "undefined" == typeof e ? "undefined" : u(e);
                return !!e && ("object" == t || "function" == t)
            }

            function i(e) {
                return !!e && "object" == ("undefined" == typeof e ? "undefined" : u(e))
            }

            function r(e) {
                return "symbol" == ("undefined" == typeof e ? "undefined" : u(e)) || i(e) && w.call(e) == s
            }

            function a(e) {
                if ("number" == typeof e) return e;
                if (r(e)) return f;
                if (o(e)) {
                    var t = "function" == typeof e.valueOf ? e.valueOf() : e;
                    e = o(t) ? t + "" : t
                }
                if ("string" != typeof e) return 0 === e ? e : +e;
                e = e.replace(d, "");
                var n = p.test(e);
                return n || m.test(e) ? b(e.slice(2), n ? 2 : 8) : l.test(e) ? f : +e
            }

            var u = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
                    return typeof e
                } : function (e) {
                    return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
                }, c = "Expected a function", f = NaN, s = "[object Symbol]", d = /^\s+|\s+$/g, l = /^[-+]0x[0-9a-f]+$/i,
                p = /^0b[01]+$/i, m = /^0o[0-7]+$/i, b = parseInt,
                v = "object" == ("undefined" == typeof t ? "undefined" : u(t)) && t && t.Object === Object && t,
                y = "object" == ("undefined" == typeof self ? "undefined" : u(self)) && self && self.Object === Object && self,
                g = v || y || Function("return this")(), h = Object.prototype, w = h.toString, k = Math.max,
                x = Math.min, j = function () {
                    return g.Date.now()
                };
            e.exports = n
        }).call(t, function () {
            return this
        }())
    }, function (e, t) {
        "use strict";

        function n(e, t) {
            var n = window.document,
                r = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver,
                a = new r(o);
            i = t, a.observe(n.documentElement, {childList: !0, subtree: !0, removedNodes: !0})
        }

        function o(e) {
            e && e.forEach(function (e) {
                var t = Array.prototype.slice.call(e.addedNodes), n = Array.prototype.slice.call(e.removedNodes),
                    o = t.concat(n).filter(function (e) {
                        return e.hasAttribute && e.hasAttribute("data-aos")
                    }).length;
                o && i()
            })
        }

        Object.defineProperty(t, "__esModule", {value: !0});
        var i = function () {
        };
        t.default = n
    }, function (e, t) {
        "use strict";

        function n(e, t) {
            if (!(e instanceof t)) throw new TypeError("Cannot call a class as a function")
        }

        function o() {
            return navigator.userAgent || navigator.vendor || window.opera || ""
        }

        Object.defineProperty(t, "__esModule", {value: !0});
        var i = function () {
                function e(e, t) {
                    for (var n = 0; n < t.length; n++) {
                        var o = t[n];
                        o.enumerable = o.enumerable || !1, o.configurable = !0, "value" in o && (o.writable = !0), Object.defineProperty(e, o.key, o)
                    }
                }

                return function (t, n, o) {
                    return n && e(t.prototype, n), o && e(t, o), t
                }
            }(),
            r = /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i,
            a = /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i,
            u = /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i,
            c = /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i,
            f = function () {
                function e() {
                    n(this, e)
                }

                return i(e, [{
                    key: "phone", value: function () {
                        var e = o();
                        return !(!r.test(e) && !a.test(e.substr(0, 4)))
                    }
                }, {
                    key: "mobile", value: function () {
                        var e = o();
                        return !(!u.test(e) && !c.test(e.substr(0, 4)))
                    }
                }, {
                    key: "tablet", value: function () {
                        return this.mobile() && !this.phone()
                    }
                }]), e
            }();
        t.default = new f
    }, function (e, t) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var n = function (e, t, n) {
            var o = e.node.getAttribute("data-aos-once");
            t > e.position ? e.node.classList.add("aos-animate") : "undefined" != typeof o && ("false" === o || !n && "true" !== o) && e.node.classList.remove("aos-animate")
        }, o = function (e, t) {
            var o = window.pageYOffset, i = window.innerHeight;
            e.forEach(function (e, r) {
                n(e, i + o, t)
            })
        };
        t.default = o
    }, function (e, t, n) {
        "use strict";

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        Object.defineProperty(t, "__esModule", {value: !0});
        var i = n(12), r = o(i), a = function (e, t) {
            return e.forEach(function (e, n) {
                e.node.classList.add("aos-init"), e.position = (0, r.default)(e.node, t.offset)
            }), e
        };
        t.default = a
    }, function (e, t, n) {
        "use strict";

        function o(e) {
            return e && e.__esModule ? e : {default: e}
        }

        Object.defineProperty(t, "__esModule", {value: !0});
        var i = n(13), r = o(i), a = function (e, t) {
            var n = 0, o = 0, i = window.innerHeight, a = {
                offset: e.getAttribute("data-aos-offset"),
                anchor: e.getAttribute("data-aos-anchor"),
                anchorPlacement: e.getAttribute("data-aos-anchor-placement")
            };
            switch (a.offset && !isNaN(a.offset) && (o = parseInt(a.offset)), a.anchor && document.querySelectorAll(a.anchor) && (e = document.querySelectorAll(a.anchor)[0]), n = (0, r.default)(e).top, a.anchorPlacement) {
                case"top-bottom":
                    break;
                case"center-bottom":
                    n += e.offsetHeight / 2;
                    break;
                case"bottom-bottom":
                    n += e.offsetHeight;
                    break;
                case"top-center":
                    n += i / 2;
                    break;
                case"bottom-center":
                    n += i / 2 + e.offsetHeight;
                    break;
                case"center-center":
                    n += i / 2 + e.offsetHeight / 2;
                    break;
                case"top-top":
                    n += i;
                    break;
                case"bottom-top":
                    n += e.offsetHeight + i;
                    break;
                case"center-top":
                    n += e.offsetHeight / 2 + i
            }
            return a.anchorPlacement || a.offset || isNaN(t) || (o = t), n + o
        };
        t.default = a
    }, function (e, t) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var n = function (e) {
            for (var t = 0, n = 0; e && !isNaN(e.offsetLeft) && !isNaN(e.offsetTop);) t += e.offsetLeft - ("BODY" != e.tagName ? e.scrollLeft : 0), n += e.offsetTop - ("BODY" != e.tagName ? e.scrollTop : 0), e = e.offsetParent;
            return {top: n, left: t}
        };
        t.default = n
    }, function (e, t) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var n = function (e) {
            return e = e || document.querySelectorAll("[data-aos]"), Array.prototype.map.call(e, function (e) {
                return {node: e}
            })
        };
        t.default = n
    }])
});

/* ========================================================================
 * Bootstrap: dropdown.js v3.3.7
 * http://getbootstrap.com/javascript/#dropdowns
 * ========================================================================
 * Copyright 2011-2016 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // DROPDOWN CLASS DEFINITION
    // =========================

    var backdrop = '.dropdown-backdrop'
    var toggle = '[data-toggle="dropdown"]'
    var Dropdown = function (element) {
        $(element).on('click.bs.dropdown', this.toggle)
    }

    Dropdown.VERSION = '3.3.7'

    function getParent($this) {
        var selector = $this.attr('data-target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
        }

        var $parent = selector && $(selector)

        return $parent && $parent.length ? $parent : $this.parent()
    }

    function clearMenus(e) {
        if (e && e.which === 3) return
        $(backdrop).remove()
        $(toggle).each(function () {
            var $this = $(this)
            var $parent = getParent($this)
            var relatedTarget = {relatedTarget: this}

            if (!$parent.hasClass('open')) return

            if (e && e.type == 'click' && /input|textarea/i.test(e.target.tagName) && $.contains($parent[0], e.target)) return

            $parent.trigger(e = $.Event('hide.bs.dropdown', relatedTarget))

            if (e.isDefaultPrevented()) return

            $this.attr('aria-expanded', 'false')
            $parent.removeClass('open').trigger($.Event('hidden.bs.dropdown', relatedTarget))
        })
    }

    Dropdown.prototype.toggle = function (e) {
        var $this = $(this)

        if ($this.is('.disabled, :disabled')) return

        var $parent = getParent($this)
        var isActive = $parent.hasClass('open')

        clearMenus()

        if (!isActive) {
            if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav').length) {
                // if mobile we use a backdrop because click events don't delegate
                $(document.createElement('div'))
                    .addClass('dropdown-backdrop')
                    .insertAfter($(this))
                    .on('click', clearMenus)
            }

            var relatedTarget = {relatedTarget: this}
            $parent.trigger(e = $.Event('show.bs.dropdown', relatedTarget))

            if (e.isDefaultPrevented()) return

            $this
                .trigger('focus')
                .attr('aria-expanded', 'true')

            $parent
                .toggleClass('open')
                .trigger($.Event('shown.bs.dropdown', relatedTarget))
        }

        return false
    }

    Dropdown.prototype.keydown = function (e) {
        if (!/(38|40|27|32)/.test(e.which) || /input|textarea/i.test(e.target.tagName)) return

        var $this = $(this)

        e.preventDefault()
        e.stopPropagation()

        if ($this.is('.disabled, :disabled')) return

        var $parent = getParent($this)
        var isActive = $parent.hasClass('open')

        if (!isActive && e.which != 27 || isActive && e.which == 27) {
            if (e.which == 27) $parent.find(toggle).trigger('focus')
            return $this.trigger('click')
        }

        var desc = ' li:not(.disabled):visible a'
        var $items = $parent.find('.dropdown-menu' + desc)

        if (!$items.length) return

        var index = $items.index(e.target)

        if (e.which == 38 && index > 0) index--         // up
        if (e.which == 40 && index < $items.length - 1) index++         // down
        if (!~index) index = 0

        $items.eq(index).trigger('focus')
    }


    // DROPDOWN PLUGIN DEFINITION
    // ==========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.dropdown')

            if (!data) $this.data('bs.dropdown', (data = new Dropdown(this)))
            if (typeof option == 'string') data[option].call($this)
        })
    }

    var old = $.fn.dropdown

    $.fn.dropdown = Plugin
    $.fn.dropdown.Constructor = Dropdown


    // DROPDOWN NO CONFLICT
    // ====================

    $.fn.dropdown.noConflict = function () {
        $.fn.dropdown = old
        return this
    }


    // APPLY TO STANDARD DROPDOWN ELEMENTS
    // ===================================

    $(document)
        .on('click.bs.dropdown.data-api', clearMenus)
        .on('click.bs.dropdown.data-api', '.dropdown form', function (e) {
            e.stopPropagation()
        })
        .on('click.bs.dropdown.data-api', toggle, Dropdown.prototype.toggle)
        .on('keydown.bs.dropdown.data-api', toggle, Dropdown.prototype.keydown)
        .on('keydown.bs.dropdown.data-api', '.dropdown-menu', Dropdown.prototype.keydown)

}(jQuery);


/**
 * BxSlider v4.1.2 - Fully loaded, responsive content slider
 * http://bxslider.com
 *
 * Copyright 2014, Steven Wanderski - http://stevenwanderski.com - http://bxcreative.com
 * Written while drinking Belgian ales and listening to jazz
 *
 * Released under the MIT license - http://opensource.org/licenses/MIT
 */
!function (t) {
    var e = {}, s = {
        mode: "horizontal",
        slideSelector: "",
        infiniteLoop: !0,
        hideControlOnEnd: !1,
        speed: 500,
        easing: null,
        slideMargin: 0,
        startSlide: 0,
        randomStart: !1,
        captions: !1,
        ticker: !1,
        tickerHover: !1,
        adaptiveHeight: !1,
        adaptiveHeightSpeed: 500,
        video: !1,
        useCSS: !0,
        preloadImages: "visible",
        responsive: !0,
        slideZIndex: 50,
        touchEnabled: !0,
        swipeThreshold: 50,
        oneToOneTouch: !0,
        preventDefaultSwipeX: !0,
        preventDefaultSwipeY: !1,
        pager: !0,
        pagerType: "full",
        pagerShortSeparator: " / ",
        pagerSelector: null,
        buildPager: null,
        pagerCustom: null,
        controls: !0,
        nextText: "Next",
        prevText: "Prev",
        nextSelector: null,
        prevSelector: null,
        autoControls: !1,
        startText: "Start",
        stopText: "Stop",
        autoControlsCombine: !1,
        autoControlsSelector: null,
        auto: !1,
        pause: 4e3,
        autoStart: !0,
        autoDirection: "next",
        autoHover: !1,
        autoDelay: 0,
        minSlides: 1,
        maxSlides: 1,
        moveSlides: 0,
        slideWidth: 0,
        onSliderLoad: function () {
        },
        onSlideBefore: function () {
        },
        onSlideAfter: function () {
        },
        onSlideNext: function () {
        },
        onSlidePrev: function () {
        },
        onSliderResize: function () {
        }
    };
    t.fn.bxSlider = function (n) {
        if (0 == this.length) return this;
        if (this.length > 1) return this.each(function () {
            t(this).bxSlider(n)
        }), this;
        var o = {}, r = this;
        e.el = this;
        var a = t(window).width(), l = t(window).height(), d = function () {
            o.settings = t.extend({}, s, n), o.settings.slideWidth = parseInt(o.settings.slideWidth), o.children = r.children(o.settings.slideSelector), o.children.length < o.settings.minSlides && (o.settings.minSlides = o.children.length), o.children.length < o.settings.maxSlides && (o.settings.maxSlides = o.children.length), o.settings.randomStart && (o.settings.startSlide = Math.floor(Math.random() * o.children.length)), o.active = {index: o.settings.startSlide}, o.carousel = o.settings.minSlides > 1 || o.settings.maxSlides > 1, o.carousel && (o.settings.preloadImages = "all"), o.minThreshold = o.settings.minSlides * o.settings.slideWidth + (o.settings.minSlides - 1) * o.settings.slideMargin, o.maxThreshold = o.settings.maxSlides * o.settings.slideWidth + (o.settings.maxSlides - 1) * o.settings.slideMargin, o.working = !1, o.controls = {}, o.interval = null, o.animProp = "vertical" == o.settings.mode ? "top" : "left", o.usingCSS = o.settings.useCSS && "fade" != o.settings.mode && function () {
                var t = document.createElement("div"),
                    e = ["WebkitPerspective", "MozPerspective", "OPerspective", "msPerspective"];
                for (var i in e) if (void 0 !== t.style[e[i]]) return o.cssPrefix = e[i].replace("Perspective", "").toLowerCase(), o.animProp = "-" + o.cssPrefix + "-transform", !0;
                return !1
            }(), "vertical" == o.settings.mode && (o.settings.maxSlides = o.settings.minSlides), r.data("origStyle", r.attr("style")), r.children(o.settings.slideSelector).each(function () {
                t(this).data("origStyle", t(this).attr("style"))
            }), c()
        }, c = function () {
            r.wrap('<div class="bx-wrapper"><div class="bx-viewport"></div></div>'), o.viewport = r.parent(), o.loader = t('<div class="bx-loading" />'), o.viewport.prepend(o.loader), r.css({
                width: "horizontal" == o.settings.mode ? 100 * o.children.length + 215 + "%" : "auto",
                position: "relative"
            }), o.usingCSS && o.settings.easing ? r.css("-" + o.cssPrefix + "-transition-timing-function", o.settings.easing) : o.settings.easing || (o.settings.easing = "swing"), f(), o.viewport.css({
                width: "100%",
                overflow: "hidden",
                position: "relative"
            }), o.viewport.parent().css({maxWidth: p()}), o.settings.pager || o.viewport.parent().css({margin: "0 auto 0px"}), o.children.css({
                "float": "horizontal" == o.settings.mode ? "left" : "none",
                listStyle: "none",
                position: "relative"
            }), o.children.css("width", u()), "horizontal" == o.settings.mode && o.settings.slideMargin > 0 && o.children.css("marginRight", o.settings.slideMargin), "vertical" == o.settings.mode && o.settings.slideMargin > 0 && o.children.css("marginBottom", o.settings.slideMargin), "fade" == o.settings.mode && (o.children.css({
                position: "absolute",
                zIndex: 0,
                display: "none"
            }), o.children.eq(o.settings.startSlide).css({
                zIndex: o.settings.slideZIndex,
                display: "block"
            })), o.controls.el = t('<div class="bx-controls" />'), o.settings.captions && P(), o.active.last = o.settings.startSlide == x() - 1, o.settings.video && r.fitVids();
            var e = o.children.eq(o.settings.startSlide);
            "all" == o.settings.preloadImages && (e = o.children), o.settings.ticker ? o.settings.pager = !1 : (o.settings.pager && T(), o.settings.controls && C(), o.settings.auto && o.settings.autoControls && E(), (o.settings.controls || o.settings.autoControls || o.settings.pager) && o.viewport.after(o.controls.el)), g(e, h)
        }, g = function (e, i) {
            var s = e.find("img, iframe").length;
            if (0 == s) return i(), void 0;
            var n = 0;
            e.find("img, iframe").each(function () {
                t(this).one("load", function () {
                    ++n == s && i()
                }).each(function () {
                    this.complete && t(this).load()
                })
            })
        }, h = function () {
            if (o.settings.infiniteLoop && "fade" != o.settings.mode && !o.settings.ticker) {
                var e = "vertical" == o.settings.mode ? o.settings.minSlides : o.settings.maxSlides,
                    i = o.children.slice(0, e).clone().addClass("bx-clone"),
                    s = o.children.slice(-e).clone().addClass("bx-clone");
                r.append(i).prepend(s)
            }
            o.loader.remove(), S(), "vertical" == o.settings.mode && (o.settings.adaptiveHeight = !0), o.viewport.height(v()), r.redrawSlider(), o.settings.onSliderLoad(o.active.index), o.initialized = !0, o.settings.responsive && t(window).bind("resize", Z), o.settings.auto && o.settings.autoStart && H(), o.settings.ticker && L(), o.settings.pager && q(o.settings.startSlide), o.settings.controls && W(), o.settings.touchEnabled && !o.settings.ticker && O()
        }, v = function () {
            var e = 0, s = t();
            if ("vertical" == o.settings.mode || o.settings.adaptiveHeight) if (o.carousel) {
                var n = 1 == o.settings.moveSlides ? o.active.index : o.active.index * m();
                for (s = o.children.eq(n), i = 1; i <= o.settings.maxSlides - 1; i++) s = n + i >= o.children.length ? s.add(o.children.eq(i - 1)) : s.add(o.children.eq(n + i))
            } else s = o.children.eq(o.active.index); else s = o.children;
            return "vertical" == o.settings.mode ? (s.each(function () {
                e += t(this).outerHeight()
            }), o.settings.slideMargin > 0 && (e += o.settings.slideMargin * (o.settings.minSlides - 1))) : e = Math.max.apply(Math, s.map(function () {
                return t(this).outerHeight(!1)
            }).get()), e
        }, p = function () {
            var t = "100%";
            return o.settings.slideWidth > 0 && (t = "horizontal" == o.settings.mode ? o.settings.maxSlides * o.settings.slideWidth + (o.settings.maxSlides - 1) * o.settings.slideMargin : o.settings.slideWidth), t
        }, u = function () {
            var t = o.settings.slideWidth, e = o.viewport.width();
            return 0 == o.settings.slideWidth || o.settings.slideWidth > e && !o.carousel || "vertical" == o.settings.mode ? t = e : o.settings.maxSlides > 1 && "horizontal" == o.settings.mode && (e > o.maxThreshold || e < o.minThreshold && (t = (e - o.settings.slideMargin * (o.settings.minSlides - 1)) / o.settings.minSlides)), t
        }, f = function () {
            var t = 1;
            if ("horizontal" == o.settings.mode && o.settings.slideWidth > 0) if (o.viewport.width() < o.minThreshold) t = o.settings.minSlides; else if (o.viewport.width() > o.maxThreshold) t = o.settings.maxSlides; else {
                var e = o.children.first().width();
                t = Math.floor(o.viewport.width() / e)
            } else "vertical" == o.settings.mode && (t = o.settings.minSlides);
            return t
        }, x = function () {
            var t = 0;
            if (o.settings.moveSlides > 0) if (o.settings.infiniteLoop) t = o.children.length / m(); else for (var e = 0, i = 0; e < o.children.length;) ++t, e = i + f(), i += o.settings.moveSlides <= f() ? o.settings.moveSlides : f(); else t = Math.ceil(o.children.length / f());
            return t
        }, m = function () {
            return o.settings.moveSlides > 0 && o.settings.moveSlides <= f() ? o.settings.moveSlides : f()
        }, S = function () {
            if (o.children.length > o.settings.maxSlides && o.active.last && !o.settings.infiniteLoop) {
                if ("horizontal" == o.settings.mode) {
                    var t = o.children.last(), e = t.position();
                    b(-(e.left - (o.viewport.width() - t.width())), "reset", 0)
                } else if ("vertical" == o.settings.mode) {
                    var i = o.children.length - o.settings.minSlides, e = o.children.eq(i).position();
                    b(-e.top, "reset", 0)
                }
            } else {
                var e = o.children.eq(o.active.index * m()).position();
                o.active.index == x() - 1 && (o.active.last = !0), void 0 != e && ("horizontal" == o.settings.mode ? b(-e.left, "reset", 0) : "vertical" == o.settings.mode && b(-e.top, "reset", 0))
            }
        }, b = function (t, e, i, s) {
            if (o.usingCSS) {
                var n = "vertical" == o.settings.mode ? "translate3d(0, " + t + "px, 0)" : "translate3d(" + t + "px, 0, 0)";
                r.css("-" + o.cssPrefix + "-transition-duration", i / 1e3 + "s"), "slide" == e ? (r.css(o.animProp, n), r.bind("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd", function () {
                    r.unbind("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd"), D()
                })) : "reset" == e ? r.css(o.animProp, n) : "ticker" == e && (r.css("-" + o.cssPrefix + "-transition-timing-function", "linear"), r.css(o.animProp, n), r.bind("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd", function () {
                    r.unbind("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd"), b(s.resetValue, "reset", 0), N()
                }))
            } else {
                var a = {};
                a[o.animProp] = t, "slide" == e ? r.animate(a, i, o.settings.easing, function () {
                    D()
                }) : "reset" == e ? r.css(o.animProp, t) : "ticker" == e && r.animate(a, speed, "linear", function () {
                    b(s.resetValue, "reset", 0), N()
                })
            }
        }, w = function () {
            for (var e = "", i = x(), s = 0; i > s; s++) {
                var n = "";
                o.settings.buildPager && t.isFunction(o.settings.buildPager) ? (n = o.settings.buildPager(s), o.pagerEl.addClass("bx-custom-pager")) : (n = s + 1, o.pagerEl.addClass("bx-default-pager")), e += '<div class="bx-pager-item"><a href="" data-slide-index="' + s + '" class="bx-pager-link">' + n + "</a></div>"
            }
            o.pagerEl.html(e)
        }, T = function () {
            o.settings.pagerCustom ? o.pagerEl = t(o.settings.pagerCustom) : (o.pagerEl = t('<div class="bx-pager" />'), o.settings.pagerSelector ? t(o.settings.pagerSelector).html(o.pagerEl) : o.controls.el.addClass("bx-has-pager").append(o.pagerEl), w()), o.pagerEl.on("click", "a", I)
        }, C = function () {
            o.controls.next = t('<a class="bx-next" href="">' + o.settings.nextText + "</a>"), o.controls.prev = t('<a class="bx-prev" href="">' + o.settings.prevText + "</a>"), o.controls.next.bind("click", y), o.controls.prev.bind("click", z), o.settings.nextSelector && t(o.settings.nextSelector).append(o.controls.next), o.settings.prevSelector && t(o.settings.prevSelector).append(o.controls.prev), o.settings.nextSelector || o.settings.prevSelector || (o.controls.directionEl = t('<div class="bx-controls-direction" />'), o.controls.directionEl.append(o.controls.prev).append(o.controls.next), o.controls.el.addClass("bx-has-controls-direction").append(o.controls.directionEl))
        }, E = function () {
            o.controls.start = t('<div class="bx-controls-auto-item"><a class="bx-start" href="">' + o.settings.startText + "</a></div>"), o.controls.stop = t('<div class="bx-controls-auto-item"><a class="bx-stop" href="">' + o.settings.stopText + "</a></div>"), o.controls.autoEl = t('<div class="bx-controls-auto" />'), o.controls.autoEl.on("click", ".bx-start", k), o.controls.autoEl.on("click", ".bx-stop", M), o.settings.autoControlsCombine ? o.controls.autoEl.append(o.controls.start) : o.controls.autoEl.append(o.controls.start).append(o.controls.stop), o.settings.autoControlsSelector ? t(o.settings.autoControlsSelector).html(o.controls.autoEl) : o.controls.el.addClass("bx-has-controls-auto").append(o.controls.autoEl), A(o.settings.autoStart ? "stop" : "start")
        }, P = function () {
            o.children.each(function () {
                var e = t(this).find("img:first").attr("title");
                void 0 != e && ("" + e).length && t(this).append('<div class="bx-caption"><span>' + e + "</span></div>")
            })
        }, y = function (t) {
            o.settings.auto && r.stopAuto(), r.goToNextSlide(), t.preventDefault()
        }, z = function (t) {
            o.settings.auto && r.stopAuto(), r.goToPrevSlide(), t.preventDefault()
        }, k = function (t) {
            r.startAuto(), t.preventDefault()
        }, M = function (t) {
            r.stopAuto(), t.preventDefault()
        }, I = function (e) {
            o.settings.auto && r.stopAuto();
            var i = t(e.currentTarget), s = parseInt(i.attr("data-slide-index"));
            s != o.active.index && r.goToSlide(s), e.preventDefault()
        }, q = function (e) {
            var i = o.children.length;
            return "short" == o.settings.pagerType ? (o.settings.maxSlides > 1 && (i = Math.ceil(o.children.length / o.settings.maxSlides)), o.pagerEl.html(e + 1 + o.settings.pagerShortSeparator + i), void 0) : (o.pagerEl.find("a").removeClass("active"), o.pagerEl.each(function (i, s) {
                t(s).find("a").eq(e).addClass("active")
            }), void 0)
        }, D = function () {
            if (o.settings.infiniteLoop) {
                var t = "";
                0 == o.active.index ? t = o.children.eq(0).position() : o.active.index == x() - 1 && o.carousel ? t = o.children.eq((x() - 1) * m()).position() : o.active.index == o.children.length - 1 && (t = o.children.eq(o.children.length - 1).position()), t && ("horizontal" == o.settings.mode ? b(-t.left, "reset", 0) : "vertical" == o.settings.mode && b(-t.top, "reset", 0))
            }
            o.working = !1, o.settings.onSlideAfter(o.children.eq(o.active.index), o.oldIndex, o.active.index)
        }, A = function (t) {
            o.settings.autoControlsCombine ? o.controls.autoEl.html(o.controls[t]) : (o.controls.autoEl.find("a").removeClass("active"), o.controls.autoEl.find("a:not(.bx-" + t + ")").addClass("active"))
        }, W = function () {
            1 == x() ? (o.controls.prev.addClass("disabled"), o.controls.next.addClass("disabled")) : !o.settings.infiniteLoop && o.settings.hideControlOnEnd && (0 == o.active.index ? (o.controls.prev.addClass("disabled"), o.controls.next.removeClass("disabled")) : o.active.index == x() - 1 ? (o.controls.next.addClass("disabled"), o.controls.prev.removeClass("disabled")) : (o.controls.prev.removeClass("disabled"), o.controls.next.removeClass("disabled")))
        }, H = function () {
            o.settings.autoDelay > 0 ? setTimeout(r.startAuto, o.settings.autoDelay) : r.startAuto(), o.settings.autoHover && r.hover(function () {
                o.interval && (r.stopAuto(!0), o.autoPaused = !0)
            }, function () {
                o.autoPaused && (r.startAuto(!0), o.autoPaused = null)
            })
        }, L = function () {
            var e = 0;
            if ("next" == o.settings.autoDirection) r.append(o.children.clone().addClass("bx-clone")); else {
                r.prepend(o.children.clone().addClass("bx-clone"));
                var i = o.children.first().position();
                e = "horizontal" == o.settings.mode ? -i.left : -i.top
            }
            b(e, "reset", 0), o.settings.pager = !1, o.settings.controls = !1, o.settings.autoControls = !1, o.settings.tickerHover && !o.usingCSS && o.viewport.hover(function () {
                r.stop()
            }, function () {
                var e = 0;
                o.children.each(function () {
                    e += "horizontal" == o.settings.mode ? t(this).outerWidth(!0) : t(this).outerHeight(!0)
                });
                var i = o.settings.speed / e, s = "horizontal" == o.settings.mode ? "left" : "top",
                    n = i * (e - Math.abs(parseInt(r.css(s))));
                N(n)
            }), N()
        }, N = function (t) {
            speed = t ? t : o.settings.speed;
            var e = {left: 0, top: 0}, i = {left: 0, top: 0};
            "next" == o.settings.autoDirection ? e = r.find(".bx-clone").first().position() : i = o.children.first().position();
            var s = "horizontal" == o.settings.mode ? -e.left : -e.top,
                n = "horizontal" == o.settings.mode ? -i.left : -i.top, a = {resetValue: n};
            b(s, "ticker", speed, a)
        }, O = function () {
            o.touch = {start: {x: 0, y: 0}, end: {x: 0, y: 0}}, o.viewport.bind("touchstart", X)
        }, X = function (t) {
            if (o.working) t.preventDefault(); else {
                o.touch.originalPos = r.position();
                var e = t.originalEvent;
                o.touch.start.x = e.changedTouches[0].pageX, o.touch.start.y = e.changedTouches[0].pageY, o.viewport.bind("touchmove", Y), o.viewport.bind("touchend", V)
            }
        }, Y = function (t) {
            var e = t.originalEvent, i = Math.abs(e.changedTouches[0].pageX - o.touch.start.x),
                s = Math.abs(e.changedTouches[0].pageY - o.touch.start.y);
            if (3 * i > s && o.settings.preventDefaultSwipeX ? t.preventDefault() : 3 * s > i && o.settings.preventDefaultSwipeY && t.preventDefault(), "fade" != o.settings.mode && o.settings.oneToOneTouch) {
                var n = 0;
                if ("horizontal" == o.settings.mode) {
                    var r = e.changedTouches[0].pageX - o.touch.start.x;
                    n = o.touch.originalPos.left + r
                } else {
                    var r = e.changedTouches[0].pageY - o.touch.start.y;
                    n = o.touch.originalPos.top + r
                }
                b(n, "reset", 0)
            }
        }, V = function (t) {
            o.viewport.unbind("touchmove", Y);
            var e = t.originalEvent, i = 0;
            if (o.touch.end.x = e.changedTouches[0].pageX, o.touch.end.y = e.changedTouches[0].pageY, "fade" == o.settings.mode) {
                var s = Math.abs(o.touch.start.x - o.touch.end.x);
                s >= o.settings.swipeThreshold && (o.touch.start.x > o.touch.end.x ? r.goToNextSlide() : r.goToPrevSlide(), r.stopAuto())
            } else {
                var s = 0;
                "horizontal" == o.settings.mode ? (s = o.touch.end.x - o.touch.start.x, i = o.touch.originalPos.left) : (s = o.touch.end.y - o.touch.start.y, i = o.touch.originalPos.top), !o.settings.infiniteLoop && (0 == o.active.index && s > 0 || o.active.last && 0 > s) ? b(i, "reset", 200) : Math.abs(s) >= o.settings.swipeThreshold ? (0 > s ? r.goToNextSlide() : r.goToPrevSlide(), r.stopAuto()) : b(i, "reset", 200)
            }
            o.viewport.unbind("touchend", V)
        }, Z = function () {
            var e = t(window).width(), i = t(window).height();
            (a != e || l != i) && (a = e, l = i, r.redrawSlider(), o.settings.onSliderResize.call(r, o.active.index))
        };
        return r.goToSlide = function (e, i) {
            if (!o.working && o.active.index != e) if (o.working = !0, o.oldIndex = o.active.index, o.active.index = 0 > e ? x() - 1 : e >= x() ? 0 : e, o.settings.onSlideBefore(o.children.eq(o.active.index), o.oldIndex, o.active.index), "next" == i ? o.settings.onSlideNext(o.children.eq(o.active.index), o.oldIndex, o.active.index) : "prev" == i && o.settings.onSlidePrev(o.children.eq(o.active.index), o.oldIndex, o.active.index), o.active.last = o.active.index >= x() - 1, o.settings.pager && q(o.active.index), o.settings.controls && W(), "fade" == o.settings.mode) o.settings.adaptiveHeight && o.viewport.height() != v() && o.viewport.animate({height: v()}, o.settings.adaptiveHeightSpeed), o.children.filter(":visible").fadeOut(o.settings.speed).css({zIndex: 0}), o.children.eq(o.active.index).css("zIndex", o.settings.slideZIndex + 1).fadeIn(o.settings.speed, function () {
                t(this).css("zIndex", o.settings.slideZIndex), D()
            }); else {
                o.settings.adaptiveHeight && o.viewport.height() != v() && o.viewport.animate({height: v()}, o.settings.adaptiveHeightSpeed);
                var s = 0, n = {left: 0, top: 0};
                if (!o.settings.infiniteLoop && o.carousel && o.active.last) if ("horizontal" == o.settings.mode) {
                    var a = o.children.eq(o.children.length - 1);
                    n = a.position(), s = o.viewport.width() - a.outerWidth()
                } else {
                    var l = o.children.length - o.settings.minSlides;
                    n = o.children.eq(l).position()
                } else if (o.carousel && o.active.last && "prev" == i) {
                    var d = 1 == o.settings.moveSlides ? o.settings.maxSlides - m() : (x() - 1) * m() - (o.children.length - o.settings.maxSlides),
                        a = r.children(".bx-clone").eq(d);
                    n = a.position()
                } else if ("next" == i && 0 == o.active.index) n = r.find("> .bx-clone").eq(o.settings.maxSlides).position(), o.active.last = !1; else if (e >= 0) {
                    var c = e * m();
                    n = o.children.eq(c).position()
                }
                if ("undefined" != typeof n) {
                    var g = "horizontal" == o.settings.mode ? -(n.left - s) : -n.top;
                    b(g, "slide", o.settings.speed)
                }
            }
        }, r.goToNextSlide = function () {
            if (o.settings.infiniteLoop || !o.active.last) {
                var t = parseInt(o.active.index) + 1;
                r.goToSlide(t, "next")
            }
        }, r.goToPrevSlide = function () {
            if (o.settings.infiniteLoop || 0 != o.active.index) {
                var t = parseInt(o.active.index) - 1;
                r.goToSlide(t, "prev")
            }
        }, r.startAuto = function (t) {
            o.interval || (o.interval = setInterval(function () {
                "next" == o.settings.autoDirection ? r.goToNextSlide() : r.goToPrevSlide()
            }, o.settings.pause), o.settings.autoControls && 1 != t && A("stop"))
        }, r.stopAuto = function (t) {
            o.interval && (clearInterval(o.interval), o.interval = null, o.settings.autoControls && 1 != t && A("start"))
        }, r.getCurrentSlide = function () {
            return o.active.index
        }, r.getCurrentSlideElement = function () {
            return o.children.eq(o.active.index)
        }, r.getSlideCount = function () {
            return o.children.length
        }, r.redrawSlider = function () {
            o.children.add(r.find(".bx-clone")).outerWidth(u()), o.viewport.css("height", v()), o.settings.ticker || S(), o.active.last && (o.active.index = x() - 1), o.active.index >= x() && (o.active.last = !0), o.settings.pager && !o.settings.pagerCustom && (w(), q(o.active.index))
        }, r.destroySlider = function () {
            o.initialized && (o.initialized = !1, t(".bx-clone", this).remove(), o.children.each(function () {
                void 0 != t(this).data("origStyle") ? t(this).attr("style", t(this).data("origStyle")) : t(this).removeAttr("style")
            }), void 0 != t(this).data("origStyle") ? this.attr("style", t(this).data("origStyle")) : t(this).removeAttr("style"), t(this).unwrap().unwrap(), o.controls.el && o.controls.el.remove(), o.controls.next && o.controls.next.remove(), o.controls.prev && o.controls.prev.remove(), o.pagerEl && o.settings.controls && o.pagerEl.remove(), t(".bx-caption", this).remove(), o.controls.autoEl && o.controls.autoEl.remove(), clearInterval(o.interval), o.settings.responsive && t(window).unbind("resize", Z))
        }, r.reloadSlider = function (t) {
            void 0 != t && (n = t), r.destroySlider(), d()
        }, d(), this
    }
}(jQuery);

// Sticky Plugin v1.0.0 for jQuery
// =============
// Author: Anthony Garand
// Improvements by German M. Bravo (Kronuz) and Ruud Kamphuis (ruudk)
// Improvements by Leonardo C. Daronco (daronco)
// Created: 2/14/2011
// Date: 2/12/2012
// Website: http://labs.anthonygarand.com/sticky
// Description: Makes an element on the page stick on the screen as you scroll
//       It will only set the 'top' and 'position' of your element, you
//       might need to adjust the width in some cases.

(function ($) {
    var defaults = {
            topSpacing: 0,
            bottomSpacing: 0,
            className: 'is-sticky',
            wrapperClassName: 'sticky-wrapper',
            center: false,
            getWidthFrom: ''
        },
        $window = $(window),
        $document = $(document),
        sticked = [],
        windowHeight = $window.height(),
        scroller = function () {
            var scrollTop = $window.scrollTop(),
                documentHeight = $document.height(),
                dwh = documentHeight - windowHeight,
                extra = (scrollTop > dwh) ? dwh - scrollTop : 0;

            for (var i = 0; i < sticked.length; i++) {
                var s = sticked[i],
                    elementTop = s.stickyWrapper.offset().top,
                    etse = elementTop - s.topSpacing - extra;

                if (scrollTop <= etse) {
                    if (s.currentTop !== null) {
                        s.stickyElement
                            .css('position', '')
                            .css('top', '');
                        s.stickyElement.parent().removeClass(s.className);
                        s.currentTop = null;
                    }
                } else {
                    var newTop = documentHeight - s.stickyElement.outerHeight()
                        - s.topSpacing - s.bottomSpacing - scrollTop - extra;
                    if (newTop < 0) {
                        newTop = newTop + s.topSpacing;
                    } else {
                        newTop = s.topSpacing;
                    }
                    if (s.currentTop != newTop) {
                        s.stickyElement
                            .css('position', 'fixed')
                            .css('top', newTop);

                        if (typeof s.getWidthFrom !== 'undefined') {
                            s.stickyElement.css('width', $(s.getWidthFrom).width());
                        }

                        s.stickyElement.parent().addClass(s.className);
                        s.currentTop = newTop;
                    }
                }
            }
        },
        resizer = function () {
            windowHeight = $window.height();
        },
        methods = {
            init: function (options) {
                var o = $.extend(defaults, options);
                return this.each(function () {
                    var stickyElement = $(this);

                    var stickyId = stickyElement.attr('id');
                    var wrapper = $('<div></div>')
                        .attr('id', stickyId + '-sticky-wrapper')
                        .addClass(o.wrapperClassName);
                    stickyElement.wrapAll(wrapper);

                    if (o.center) {
                        stickyElement.parent().css({
                            width: stickyElement.outerWidth(),
                            marginLeft: "auto",
                            marginRight: "auto"
                        });
                    }

                    if (stickyElement.css("float") == "right") {
                        stickyElement.css({"float": "none"}).parent().css({"float": "right"});
                    }

                    var stickyWrapper = stickyElement.parent();
                    stickyWrapper.css('height', stickyElement.outerHeight());
                    sticked.push({
                        topSpacing: o.topSpacing,
                        bottomSpacing: o.bottomSpacing,
                        stickyElement: stickyElement,
                        currentTop: null,
                        stickyWrapper: stickyWrapper,
                        className: o.className,
                        getWidthFrom: o.getWidthFrom
                    });
                });
            },
            update: scroller
        };

    // should be more efficient than using $window.scroll(scroller) and $window.resize(resizer):
    if (window.addEventListener) {
        window.addEventListener('scroll', scroller, false);
        window.addEventListener('resize', resizer, false);
    } else if (window.attachEvent) {
        window.attachEvent('onscroll', scroller);
        window.attachEvent('onresize', resizer);
    }

    $.fn.sticky = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.sticky');
        }
    };
    $(function () {
        setTimeout(scroller, 0);
    });
})(jQuery);


$(function () {

    // 상단 메뉴 버튼
    $('#header .btn-m').click(function () {
        $('body').addClass('menu-on');
    })
    $('.m-gnb ,.bg-gnb').click(function () {
        $('body').removeClass('menu-on');
    })

    // 이벤트 상단
    var eventSwiper = new Swiper('.rent-after-view .swiper-container', {
        pagination: {
            el: '.rent-after-view .swiper-pagination',
            type: 'fraction',
        },
        navigation: {
            nextEl: '.rent-after-view .swiper-button-next',
            prevEl: '.rent-after-view .swiper-button-prev',
        },
    });

    // 이벤트 상단
    var eventSwiper = new Swiper('.card-slider .swiper-container', {
        pagination: {
            el: '.card-slider .swiper-pagination',
            type: 'fraction',
        },
        navigation: {
            nextEl: '.card-slider .swiper-button-next',
            prevEl: '.card-slider .swiper-button-prev',
        },
    });

    // 마이페이지 > 카드
    var paymentCardSwiper = new Swiper('.event-head .swiper-container', {
        pagination: {
            el: '.event-head .swiper-pagination',
            type: 'fraction',
        },
        navigation: {
            nextEl: '.event-head .swiper-button-next',
            prevEl: '.event-head .swiper-button-prev',
        },
    });


    // 차량 상세 이미지 슬라이더
    var carSwiper = new Swiper('.rent-detail-wrap .swiper-container', {
        pagination: {
            el: '.rent-detail-wrap .swiper-pagination',
            type: 'fraction',
        },
        navigation: {
            nextEl: '.rent-detail-wrap .swiper-button-next',
            prevEl: '.rent-detail-wrap .swiper-button-prev',
        },
    });
    // 윈도우 사이즈(모바일 컨트롤)
    var windowSize = $(window).width();

    function fNwindowSize() {
        windowSize = $(window).width();
    }

    $(window).resize(function () {
        fNwindowSize();
    })
    fNwindowSize();


    $('.top-request .in .req-area .btn-req').click(function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            $(this).next().stop().slideUp(300);
            $('.sub-container1060').removeClass('bg-active');
            $('.sub-container1060').find('.bg-req').remove();
        } else {
            $(this).addClass('active');
            $(this).next().stop().slideDown(300);
            $('.sub-container1060').addClass('bg-active');
            $('.sub-container1060').append("<p class='bg-req'></p>");
        }
    });
    $(document).on('click', '.bg-req', function () {
        $('.top-request .in .req-area .btn-req').removeClass('active');
        $('.top-request .in .req-area .btn-req').next().stop().slideUp(300);
        $('.sub-container1060').removeClass('bg-active');
        $('.sub-container1060').find('.bg-req').remove();
    })

    $('.faq .q, .notice .head').click(function () {
        if (!$(this).closest('li').hasClass('active')) {
            $(this).closest('li').addClass('active');
            $(this).next().stop().slideDown(300);
        } else {
            $(this).closest('li').removeClass('active');
            $(this).next().stop().slideUp(300);
        }
    })


    // 견적요청 > 요청정보 상단에 붙는 기능 제거 - 내용넘칠때 스크롤 이슈
    if (windowSize < 768) {
        // $(".top-request").sticky({topSpacing:56});
    }

    // 차량 상세 이미지 슬라이더
    var carSwiper = new Swiper('.main .slider-box .swiper-container', {
        pagination: {
            el: '.main .slider-box .swiper-pagination',
            type: 'fraction',
        },
        navigation: {
            nextEl: '.main .slider-box .swiper-button-next',
            prevEl: '.main .slider-box .swiper-button-prev',
        },
    });


});
