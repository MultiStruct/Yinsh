import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { ToggleModule } from './toggle-module/ToggleModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TooltipModule,
	ToggleModule,
	EndScreenModule
];

// The list of toggles displayed in the options of the viewer
export const options = [
    ToggleModule.defineToggle({
        toggle: 'coordinatesToggle',
        title: 'coordinates toggle',
        values: {
        'on': true,
        'off': false
        },
        default: false
    }),ToggleModule.defineToggle({
        toggle: 'viewerToggle',
        title: 'viewer type',
        values: {
        'lines': true,
        'hexes': false
        },
        default: true
    })
];