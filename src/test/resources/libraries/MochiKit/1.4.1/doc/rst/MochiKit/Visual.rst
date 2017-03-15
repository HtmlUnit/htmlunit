.. title:: MochiKit.Visual - visual effects

Name
====

MochiKit.Visual - visual effects


Synopsis
========

::

    // round the corners of all h1 elements
    roundClass("h1", null);

    // round the top left corner of the element with the id "title"
    roundElement("title", {corners: "tl"});

    // Add an fade effect to an element
    fade('myelement');


Description
===========

MochiKit.Visual provides visual effects and animations for HTML elements.


Dependencies
============

- :mochiref:`MochiKit.Base`
- :mochiref:`MochiKit.Iter`
- :mochiref:`MochiKit.DOM`
- :mochiref:`MochiKit.Color`
- :mochiref:`MochiKit.Position`


Overview
========

MochiKit.Visual provides different visual effects: rounded corners and
animations for your HTML elements. Rounded corners are created
completely through CSS manipulations and require no external images or
style sheets.  This implementation was adapted from Rico_. Dynamic
effects are ported from Scriptaculous_.

.. _Rico: http://www.openrico.org

.. _Scriptaculous: http://script.aculo.us


Animations & Effects
--------------------

Dynamic or animated effects are managed by a basic looping service (see
:mochiref:`Base`). The effect looping is controlled by timers that are invoked
at regular and short intervals while the effect is executing. The base looping
service takes the current system time into consideration, automatically
skipping effect steps on execution delays.

The effect classes need only handle dynamic updates based on a floating-point
number between ``0.0`` and ``1.0`` (the effect position). This effect position
may also be manipulated by :mochiref:`Transitions` to provide non-linear
updates, which may further enhance the visual effect.

The effect timer and transitions can be controlled through a set of
:mochiref:`DefaultOptions` that are available for all effect classes.


The Effects Queue
-----------------

When you create effects based on user input (mouse clicks for example), it can
create conflicts between the effects if multiple effects are running at the
same time. To manage this problem, the Queue mechanism has been introduced:
it's responsible for running the effects as you desired.

By default, you have one Queue called ``'global'``, and the effects run in
``'parallel'`` (see :mochiref:`DefaultOptions`). Every effect has a ``queue``
option to customize this. Its value can be a string, thereby using the
``global`` scope:

- `front`: the effect will be run before any other non-started effect;
- `end`: the effect will be run when all other effects have finished;
- `break`: every other effect is immediately finalized when the the effect start;
- `parallel`: the effect runs in parallel with other effects.

But you have even more control if you use an object with the following
property keys:

- `position` takes one of the values listed above;
- `scope` contains the queue name. If it's ``"global"`` the effect will use the
  default queue, otherwise you can define your own queue name. For example, if
  you add an effect on a specified element, you may use the element id as scope;
- `limit` defines how many effects can be stored in the queue at a single time.
  If an effect is added when the limit has been reached, it will never be run
  (it's lost).


API Reference
=============

Functions
---------

:mochidef:`roundClass(tagName[, className[, options]])`:

    Rounds all of the elements that match the ``tagName`` and
    ``className`` specifiers, using the options provided.  ``tagName``
    or ``className`` can be ``null`` to match all tags or classes.
    Regarding the ``options``, see the :mochiref:`roundElement` function.

    *Availability*:
        Available in MochiKit 1.3.1+


:mochidef:`roundElement(element[, options])`:

    Immediately round the corners of the specified ``element``.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The ``options`` mapping has the following defaults:

        ========= =================
        corners   ``"all"``
        color     ``"fromElement"``
        bgColor   ``"fromParent"``
        blend     ``true``
        border    ``false``
        compact   ``false``
        ========= =================

    The ``options`` parameters can have following values:

    corners:
        Specifies which corners of the element should be rounded.
        Choices are:

        - all
        - top
        - bottom
        - tl (top left)
        - bl (bottom left)
        - tr (top right)
        - br (bottom right)

        Example:
            ``"tl br"``: top-left and bottom-right corners are rounded

    blend:
        Specifies whether the color and background color should be
        blended together to produce the border color.

    *Availability*:
        Available in MochiKit 1.3.1+


:mochidef:`toggle(element[, effect[, options]])`:

    Toggle an ``element`` between visible and invisible state using an
    effect.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    effect:
        One of the visual pairs to use:

        ============ =========================================
        ``"appear"`` :mochiref:`appear` and :mochiref:`fade`
        ``"blind"``  :mochiref:`blindUp` and :mochiref:`blindDown`
        ``"size"``   :mochiref:`grow` and :mochiref:`shrink`
        ``"slide"``  :mochiref:`slideUp` and :mochiref:`slideDown`
        ============ =========================================

    options:
        The optional effect options (see the respective effect
        functions for details).

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`tagifyText(element[, tagifyStyle])`:

    Transform the node text into ``SPAN`` nodes each containing a single
    letter. Only text nodes that are immediate children will be modified
    by this function.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    tagifyStyle:
        Style to apply to each new ``SPAN`` node, defaults to
        ``'position: relative'``.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`multiple(elements, effect[, options])`:

    Launch the same effect on a list of ``elements``.

    elements:
        A list of element ID strings or a DOM nodes (see
        :mochiref:`MochiKit.DOM.getElement`).

    effect:
        An effect class constructor function.

    options:
        The effect options (see the respective effect class). A special
        ``speed`` value will be added to the effect ``delay`` for each
        additional element in the list. This cause the effects to not run
        exactly in parallel by default.

        ========= =================
        speed     ``0.1``
        delay     ``0.0`` (initial)
        ========= =================

    *Availability*:
        Available in MochiKit 1.4+


Combination Effects
-------------------

:mochidef:`fade(element[, options])`:

    Fades an ``element`` using the :mochiref:`Opacity` effect. Once
    the effect has completed, the ``element`` will be hidden with
    :mochiref:`MochiKit.Style.hideElement` and the original element
    opacity will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        ====== =============================================
        from   ``element.opacity || 1.0``
        to     ``0.0``
        ====== =============================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`appear(element [, options])`:

    Slowly shows an ``element`` using the :mochiref:`Opacity` effect.
    If the ``element`` has ``"display: none"`` style, it will be changed
    to ``"display: block"`` with :mochiref:`MochiKit.Style.showElement`.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        ===== =============================================
        from  ``element.opacity || 0.0``
        to    ``1.0``
        ===== =============================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`puff(element [, options])`:

    Make an ``element`` double size while also fading it using the
    :mochiref:`Scale` and :mochiref:`Opacity` effects in parallel.
    Once the effect has completed, the ``element`` will be hidden with
    :mochiref:`MochiKit.Style.hideElement` and the original element
    size, position and opacity will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`blindUp(element [, options])`:

    Blind an ``element`` up, changing its vertical size to 0 using the
    :mochiref:`Scale` effect. Once the effect has completed, the
    ``element`` will be hidden with
    :mochiref:`MochiKit.Style.hideElement` and the original element
    size will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`blindDown(element [, options])`:

    Blind an ``element`` down, restoring its vertical size using the
    :mochiref:`Scale` effect. If the ``element`` has ``"display: none"``
    style, it will be changed to ``"display: block"`` with
    :mochiref:`MochiKit.Style.showElement`.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`switchOff(element [, options])`:

    A switch-off like effect, making the ``element`` disappear, using
    the :mochiref:`Opacity` and :mochiref:`Scale` effects in sequence.
    The initial :mochiref:`Opacity` effect uses a flicker (partially
    random) transformation. Once the effect has completed, the
    ``element`` will be hidden with
    :mochiref:`MochiKit.Style.hideElement` and the original element
    opacity, size and position will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`. Note that the options will
        only affect the secondary :mochiref:`Scale` effect.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`dropOut(element [, options])`:

    Make the element fall and fade using the
    :mochiref:`Move` and :mochiref:`Opacity` effects in parallel.
    Once the effect has completed, the ``element`` will be hidden with
    :mochiref:`MochiKit.Style.hideElement` and the original element
    position and opacity will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The ``distance`` option controls the number of pixels that the
        element will move downwards. See also the
        :mochiref:`DefaultOptions`.

        ======== =======
        distance ``100``
        ======== =======

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`shake(element [, options])`:

    Shake an element from left to right using a sequence of six
    :mochiref:`Move` effects. Once the effect has completed, the
    original ``element`` position will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`. Note that the options will
        only affect the last :mochiref:`Move` effect.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`slideDown(element [, options])`:

    Slide an ``element`` down using the :mochiref:`Scale` effect.
    The ``element`` must have a fixed height and contain a single
    child. If the ``element`` has ``"display: none"`` style it
    will be changed to ``"display: block"`` with
    :mochiref:`MochiKit.Style.showElement`.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`slideUp(element [, options])`:

    Slide an ``element`` up using the :mochiref:`Scale` effect.
    The ``element`` must have a fixed height and contain a single
    child. Once the effect has completed, the ``element`` will be
    hidden with :mochiref:`MochiKit.Style.hideElement` and the
    original element size will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`squish(element [, options])`:

    Reduce the horizontal and vertical sizes at the same time using
    a single :mochiref:`Scale` effect. The result is similar to the
    :mochiref:`shrink` effect with a ``top-left`` value for the
    ``direction`` option. The ``element`` should have fixed width and
    height. Once the effect has completed, the ``element`` will be
    hidden with :mochiref:`MochiKit.Style.hideElement` and the
    original element size will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`grow(element [, options])`:

    Grows an ``element`` size using :mochiref:`Scale`, :mochiref:`Move`
    and :mochiref:`Opacity` effects in parallel. The ``element`` should
    have fixed width, height and top-left position. Before the effect
    starts, the ``element`` will be shown with
    :mochiref:`MochiKit.Style.showElement` and the size and position
    values will be read.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The following options and default values control this
        effect. Note that the :mochiref:`Opacity` effect is turned
        off by default. See also the :mochiref:`DefaultOptions`.

        ================= ========================================
        direction         ``"center"``
        moveTransition    ``MochiKit.Visual.Transitions.sinoidal``
        scaleTransition   ``MochiKit.Visual.Transitions.sinoidal``
        opacityTransition ``MochiKit.Visual.Transitions.full``
        ================= ========================================

    The ``direction`` option controls the origin point of the effect.
    The following values are allowed:

        ===================== ========================================
        ``"center"``          Grows from the center
        ``"top-left"``        Grows from the top left corner
        ``"top-right"``       Grows from the top right corner
        ``"bottom-left"``     Grows from the bottom left corner
        ``"bottom-right"``    Grows from the bottom right corner
        ===================== ========================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`shrink(element [, options])`:

    Shrinks an ``element`` using :mochiref:`Scale`, :mochiref:`Move`
    and :mochiref:`Opacity` effects in parallel. The ``element`` should
    have fixed width, height and top-left position. Once the effect has
    completed, the ``element`` will be hidden with
    :mochiref:`MochiKit.Style.hideElement` and the original size and
    position will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The following options and default values control this
        effect. Note that the :mochiref:`Opacity` effect is turned
        off by default. See also the :mochiref:`DefaultOptions`.

        ================= ========================================
        direction         ``"center"``
        moveTransition    ``MochiKit.Visual.Transitions.sinoidal``
        scaleTransition   ``MochiKit.Visual.Transitions.sinoidal``
        opacityTransition ``MochiKit.Visual.Transitions.full``
        ================= ========================================

    The ``direction`` option controls the destination point of the
    effect. The following values are allowed:

        ===================== ========================================
        ``"center"``          Grows from the center
        ``"top-left"``        Grows from the top left corner
        ``"top-right"``       Grows from the top right corner
        ``"bottom-left"``     Grows from the bottom left corner
        ``"bottom-right"``    Grows from the bottom right corner
        ===================== ========================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`pulsate(element [, options])`:

    Switches the ``element`` visibility using a pulsating
    :mochiref:`Opacity` effect. The effect both starts and
    ends with a ``0`` opacity value.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The ``pulses`` option controls the number of pulses
        made during the effect. See also the
        :mochiref:`DefaultOptions`.

        ====== ========
        pulses ``5``
        ====== ========

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`fold(element [, options])`:

    Reduce first the ``element`` vertical size, and then the
    horizontal size using two :mochiref:`Scale` effects in sequence.
    The ``element`` should have both fixed width and height. Once
    the effect has completed, the ``element`` will be hidden
    with :mochiref:`MochiKit.Style.hideElement` and the original
    size and position will be restored.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`. Note that the options
        will only affect the first :mochiref:`Scale` effect.

    *Availability*:
        Available in MochiKit 1.4+


Basic Effects Classes & Constants
---------------------------------

:mochidef:`Transitions`:

    Default transition functions available for all effects. A transition
    function adjusts the current position value between 0 and 1 in order
    to achieve a non-linear sequence of position values for the effect.

    =========== ========================================
    linear      A straight linear transition.
    sinoidal    A smooth sine value transition.
    reverse     A reverse linear transition.
    flicker     A sine transition with random additions.
    wobble      A multi-period sine curve transition with 4.5 wobbles and ending with one (1).
    pulse       A multi-period triangle curve transition with 5 pulses (by default) and ending with zero (0).
    parabolic   A smooth parabolic transition.
    none        A fixed zero (0) value transition.
    full        A fixed one (1) value transition.
    =========== ========================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`DefaultOptions`:

    Default options for all effects. Note that all effects inherit
    the :mochiref:`Base` class and thereby also support a number of
    events that can be specified as callback functions among the
    effect options.

    =========== ========================================
    transition  ``MochiKit.Visual.Transitions.sinoidal``
    duration    ``1.0`` (seconds)
    fps         ``25.0``
    sync        ``false`` (only set for :mochiref:`Parallel` or :mochiref:`Sequence` effects)
    from        ``0.0``
    to          ``1.0``
    delay       ``0.0``
    queue       ``'parallel'``
    =========== ========================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Base()`:

    Base class to all effects. Define a basic looping service, use it
    for creating new effects.

    You can override the methods ``setup``, ``update`` and ``finish``.

    The class defines a number of events that will be called during effect
    life. The events are:

    - beforeStart
    - beforeSetup
    - beforeUpdate
    - afterUpdate
    - beforeFinish
    - afterFinish

    If you want to define your own callbacks, define it in the options
    parameter of the effect. Example::

        // I slide it up and then down again
        slideUp('myelement', {afterFinish: function () {
            slideDown('myelement');
        });

    Specific ``internal`` events are also available: for each one listed above,
    the same exists with an 'Internal' postfix (e.g. 'beforeStartInternal').
    Their purpose is mainly for creating your own effect and keep the user
    access to event callbacks (not overriding the library ones).

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Parallel(effects [, options])`:

    Launch a list of ``effects`` in parallel.

    effects:
        An array of instantiated effect objects. Note that they *must*
        all have ``sync`` set to ``true``.

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Sequence(effects [, options])`:

    Launch a list of ``effects`` in sequence, one after the other.

    effects:
        An array of instantiated effect objects. Note that they *must*
        all have ``sync`` set to ``true``.

    options:
        See :mochiref:`DefaultOptions`. Note that the default value
        for some options is different and that some of the transition
        and timing options don't make sense to override.

        =========== ========================================
        transition  ``MochiKit.Visual.Transitions.linear``
        duration    Sum of the ``duration`` for all ``effects``.
        from        Only ``0.0`` makes sense.
        to          Only ``1.0`` makes sense.
        =========== ========================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Opacity(element [, options])`:

    Change the opacity of an ``element`` progressively.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The following options and default values control this effect.
        See also the :mochiref:`DefaultOptions`.

        ====== ========
        from   ``0.0``
        to     ``1.0``
        ====== ========

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Move(element [, options])`:

    Changes the (top left) position of an ``element`` in small steps,
    creating a moving effect.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The following options and default values control this effect.
        See also the :mochiref:`DefaultOptions`.

        ========= ================
        x         ``0``
        y         ``0``
        mode      ``'relative'``
        ========= ================

    The ``mode`` option controls if the specified ``x`` and ``y``
    coordinates are ``relative`` or ``absolute`` with respect to the
    current ``element`` position.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Scale(element, percent [, options])`:

    Changes the size of an ``element``.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    percent:
        Final wanted size in percent of current size. The size will be
        reduced if the value is between 0 and 100, and raised if the
        value is above 100.

    options:
        The following options and default values control this effect.
        See also the :mochiref:`DefaultOptions`.

        ================ ============
        scaleX           ``true``
        scaleY           ``true``
        scaleContent     ``true``
        scaleFromCenter  ``false``
        scaleMode        ``"box"``
        scaleFrom        ``100.0``
        scaleTo          ``percent``
        ================ ============

    The ``scaleContent`` option controls if the element ``font-size``
    should also be scaled along with the size. The ``scaleFromCenter``
    option allows continual adjustment of the ``element`` top left
    position to keep the element centered during the size change.
    The ``scaleMode`` option is used to determine the original
    ``element`` size. It can have one of the following values:

        ================ =========================================
        ``"box"``        Uses ``offsetHeight`` and ``offsetWidth``
        ``"contents"``   Uses ``scrollHeight`` and ``scrollWidth``
        {...}            Uses ``originalHeight`` and ``originalWidth`` from the specified object
        ================ =========================================

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Highlight(element [, options])`:

    Highlights an ``element`` by flashing the background color. The
    color is first set to the ``startcolor`` and is then slowly
    morphed into the ``endcolor``, normally the original background
    color of the element.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        The following options and default values control this effect.
        See also the :mochiref:`DefaultOptions`.

        =========== ==============
        startcolor  ``'#ffff99'``
        endcolor    ``element.style.backgroundColor``
        =========== ==============

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`ScrollTo(element [, options])`:

    Scroll the window to the position of the given ``element``. Note
    that this effect only scrolls the top window and not any parent
    elements with scrollbars due to ``"overflow: auto"`` style.

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


:mochidef:`Morph(element [, options])`:

    Make a transformation to the given ``element``. It's called with the option
    ``style`` with an array holding the styles to change. It works with
    properties for size (``font-size``, ``border-width``, ...) and properties
    for color (``color``, ``background-color``, ...).

    For size, it's better to have defined the original style. You *must*
    use the same unit in the call to Morph (no translation exists between two
    different units).

    Parsed length are postfixed with: em, ex, px, in, cm, mm, pt, pc.

    Example::

        <div id="foo" style="font-size: 1em">MyDiv</div>
        ...
        Morph("foo", {"style": {"font-size": "2em"}});

    element:
        An element ID string or a DOM node (see
        :mochiref:`MochiKit.DOM.getElement`).

    options:
        See :mochiref:`DefaultOptions`.

    *Availability*:
        Available in MochiKit 1.4+


Authors
=======

- Kevin Dangoor <dangoor@gmail.com>
- Bob Ippolito <bob@redivi.com>
- Thomas Herve <therve@gmail.com>
- Round corners originally adapted from Rico <http://openrico.org/>
  (though little remains)
- Effects originally adapted from Script.aculo.us
  <http://script.aculo.us/>


Copyright
=========

Copyright 2005 Bob Ippolito <bob@redivi.com>.  This program is
dual-licensed free software; you can redistribute it and/or modify it
under the terms of the `MIT License`_ or the `Academic Free License
v2.1`_.

.. _`MIT License`: http://www.opensource.org/licenses/mit-license.php
.. _`Academic Free License v2.1`: http://www.opensource.org/licenses/afl-2.1.php

Portions adapted from `Rico`_ are available under the terms of the
`Apache License, Version 2.0`_.

Portions adapted from `Scriptaculous`_ are available under the terms
of the `MIT License`_.

.. _`Apache License, Version 2.0`: http://www.apache.org/licenses/LICENSE-2.0.html
