<script>
    import {onMount} from "svelte";
    import {rgbaToInt, intToRgba, rgbaToHex} from "../../../../utils/misc.js";

    /**
     * A reference to the value instance of this setting. It is part of the module configurable and should NOT lose its reference.
     */
    export let reference;
    /**
     * This function is passed from the parent component and is used to write the new configurable to the client.
     * This will result in a request to the server.
     */
    export let write;

    let name = reference.name;
    let rgba = intToRgba(reference.value);
    let hex = rgbaToHex(rgba);
    let colorPicker = null;
    let pickr = null;
    
    onMount(() => {
        pickr = Pickr.create({
            el: colorPicker,
            theme: "classic",
            showAlways: true,
            inline: true,
            default: hex,

            components: {
                preview: false,
                opacity: true,
                hue: true,

                interaction: {
                    hex: false,
                    rgba: false,
                    hsla: false,
                    hsva: false,
                    cmyk: false,
                    input: false,
                    clear: false,
                    save: false
                }
            }
        });

        pickr.on("change", v => {
            hex = v.toHEXA().toString();

            const [r, g, b, a] = v.toRGBA();
            rgba = [r, g, b, a * 255];
            
            reference.value = rgbaToInt(rgba);
            write();
        });
    });

    // TODO: use hex colors
    function handleValueChange(e) {
        /*         const v = e.target.value;
                if (v.length === 6) {
                    pickr.setColor(`#${v}`);
                } */

        pickr.setColor(e.target.value)

    }

    let hidden = true

    function togglePickr() {
        hidden = !hidden
    }
</script>

<div class="setting">
    <div class="name">{name}</div>
    <div class="value-spot">
        <input class="value" value={hex} on:input={handleValueChange}>
        <button class="color-pickr-button" on:click={togglePickr} style="background-color: {hex};"></button>
    </div>
    <div class="animation-fix color-picker" class:hidden={hidden}>
        <button bind:this={colorPicker}/>
    </div>
</div>

<style>
    .setting {
        display: grid;
        grid-template-areas:
            "a b"
            "c c";
        padding: 7px 10px;
    }

    .name {
        grid-area: a;
        font-weight: 500;
        color: var(--text);;
        font-size: 12px;
    }

    /* Fix glitching of settings expand animation */
    .animation-fix {
        height: 138px;
    }

    .hidden {
        height: 0px;
        display: none;
    }

    .value {
        font-weight: 500;
        color: var(--text);;
        text-align: right;
        font-size: 12px;
        cursor: text;
        text-transform: uppercase;
        background-color: transparent;
        border: none;
        padding: 0;
        margin: 0;
        margin-right: 15px;
        margin-left: auto;
        width: 70px;
    }

    .value-spot {
        grid-area: b;
        display: flex;
    
        align-items: stretch;
    }

    .color-picker {
        grid-area: c;
    }

    .color-pickr-button {
        margin-top: -2px;
        margin-bottom: -2px;
        width: 30px;
        border-radius: 3px;
        background-color: blue;
        border-style: none;

    }
    .color-pickr-button:focus {
        outline: 3px solid  #ffffff;
    }
</style>
