// Generated by dts-bundle v0.7.3
// Dependencies for this module:
//   ../../@material/base/component
//   ../../@material/ripple/component
//   ../../@material/ripple/types
//   ../../@material/base/foundation

declare module '@material/switch' {
    /**
      * @license
      * Copyright 2019 Google Inc.
      *
      * Permission is hereby granted, free of charge, to any person obtaining a copy
      * of this software and associated documentation files (the "Software"), to deal
      * in the Software without restriction, including without limitation the rights
      * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      * copies of the Software, and to permit persons to whom the Software is
      * furnished to do so, subject to the following conditions:
      *
      * The above copyright notice and this permission notice shall be included in
      * all copies or substantial portions of the Software.
      *
      * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
      * THE SOFTWARE.
      */
    export * from '@material/switch/adapter';
    export * from '@material/switch/component';
    export * from '@material/switch/constants';
    export * from '@material/switch/foundation';
}

declare module '@material/switch/adapter' {
    /**
        * Defines the shape of the adapter expected by the foundation.
        * Implement this adapter for your framework of choice to delegate updates to
        * the component in your framework of choice. See architecture documentation
        * for more details.
        * https://github.com/material-components/material-components-web/blob/master/docs/code/architecture.md
        */
    export interface MDCSwitchAdapter {
            /**
                * Adds a CSS class to the root element.
                */
            addClass(className: string): void;
            /**
                * Removes a CSS class from the root element.
                */
            removeClass(className: string): void;
            /**
                * Sets checked state of the native HTML control underlying the switch.
                */
            setNativeControlChecked(checked: boolean): void;
            /**
                * Sets the disabled state of the native HTML control underlying the switch.
                */
            setNativeControlDisabled(disabled: boolean): void;
            /**
                * Sets an attribute value of the native HTML control underlying the switch.
                */
            setNativeControlAttr(attr: string, value: string): void;
    }
}

declare module '@material/switch/component' {
    /**
      * @license
      * Copyright 2018 Google Inc.
      *
      * Permission is hereby granted, free of charge, to any person obtaining a copy
      * of this software and associated documentation files (the "Software"), to deal
      * in the Software without restriction, including without limitation the rights
      * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      * copies of the Software, and to permit persons to whom the Software is
      * furnished to do so, subject to the following conditions:
      *
      * The above copyright notice and this permission notice shall be included in
      * all copies or substantial portions of the Software.
      *
      * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
      * THE SOFTWARE.
      */
    import { MDCComponent } from '@material/base/component';
    import { MDCRipple } from '@material/ripple/component';
    import { MDCRippleCapableSurface } from '@material/ripple/types';
    import { MDCSwitchFoundation } from '@material/switch/foundation';
    export class MDCSwitch extends MDCComponent<MDCSwitchFoundation> implements MDCRippleCapableSurface {
        static attachTo(root: HTMLElement): MDCSwitch;
        destroy(): void;
        initialSyncWithDOM(): void;
        getDefaultFoundation(): MDCSwitchFoundation;
        get ripple(): MDCRipple;
        get checked(): boolean;
        set checked(checked: boolean);
        get disabled(): boolean;
        set disabled(disabled: boolean);
    }
}

declare module '@material/switch/constants' {
    /** CSS classes used by the switch. */
    const cssClasses: {
        /** Class used for a switch that is in the "checked" (on) position. */
        CHECKED: string;
        /** Class used for a switch that is disabled. */
        DISABLED: string;
    };
    /** String constants used by the switch. */
    const strings: {
        /** Aria attribute for checked or unchecked state of switch */
        ARIA_CHECKED_ATTR: string;
        /** A CSS selector used to locate the native HTML control for the switch.  */
        NATIVE_CONTROL_SELECTOR: string;
        /** A CSS selector used to locate the ripple surface element for the switch. */
        RIPPLE_SURFACE_SELECTOR: string;
    };
    export { cssClasses, strings };
}

declare module '@material/switch/foundation' {
    /**
      * @license
      * Copyright 2018 Google Inc.
      *
      * Permission is hereby granted, free of charge, to any person obtaining a copy
      * of this software and associated documentation files (the "Software"), to deal
      * in the Software without restriction, including without limitation the rights
      * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      * copies of the Software, and to permit persons to whom the Software is
      * furnished to do so, subject to the following conditions:
      *
      * The above copyright notice and this permission notice shall be included in
      * all copies or substantial portions of the Software.
      *
      * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
      * THE SOFTWARE.
      */
    import { MDCFoundation } from '@material/base/foundation';
    import { MDCSwitchAdapter } from '@material/switch/adapter';
    export class MDCSwitchFoundation extends MDCFoundation<MDCSwitchAdapter> {
        /** The string constants used by the switch. */
        static get strings(): {
            ARIA_CHECKED_ATTR: string;
            NATIVE_CONTROL_SELECTOR: string;
            RIPPLE_SURFACE_SELECTOR: string;
        };
        /** The CSS classes used by the switch. */
        static get cssClasses(): {
            CHECKED: string;
            DISABLED: string;
        };
        /** The default Adapter for the switch. */
        static get defaultAdapter(): MDCSwitchAdapter;
        constructor(adapter?: Partial<MDCSwitchAdapter>);
        /** Sets the checked state of the switch. */
        setChecked(checked: boolean): void;
        /** Sets the disabled state of the switch. */
        setDisabled(disabled: boolean): void;
        /** Handles the change event for the switch native control. */
        handleChange(evt: Event): void;
    }
    export default MDCSwitchFoundation;
}

