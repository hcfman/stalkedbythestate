SharedTemplates = function () {
	this._templates = {};
	
	this.hasTemplate = function (key) {
		return !!this._templates[key];
	};
	
	this.setTemplate = function (key, template) {
		this._templates[key] = template;
	};
	
	this.getTemplate = function (key) {
		var template = '';
		
		if ( this.hasTemplate(key) ) {
 			template = this._templates[key];
		}
		
		var tmpl = new JSTemplate( template, key );
		
		if (tmpl.isEmpty) {
			console.warn('Empty template for key: ' + key);
		}else{
			console.log('Created template for key: ' + key);
		}
		
		return tmpl;
	};
};

SharedTemplates = new SharedTemplates();

/**
	Javascript Template
	
	Use string templates in javascript.
*/
JSTemplate = function ( template, name ) {
	this.name 		= name || 'Untitled';
	this.content 	= template.toString();
	this.isEmpty	= this.content.match(/^\s*?$/);
	this.properties = {};
	
	this.reset = function () {
		this.properties = {};
	};
	
	/**
		Set a properties value so it can be used inside the template
	*/
	this.set = function (key, value) {
		var type = typeof(value);
		
		if (type == 'number' && value.toString) {
			value = value.toString();
		}
		this.properties[key] = value;
	};
	/**
		Gets a set property value
	*/
	this.get = function (key) {
		var value = null;
		if (this.properties[key]) {
			value = this.properties[key];
		}
		return value;
	};
	
	/**
		Sets all properties of the object for use in the template.
		Methods are not copied.
	*/
	this.setObject = function (obj) {
		for (var property in obj) {
			var value = obj[property];
			if ( typeof(value) != 'function' ) {
				this.set(property, value);
			}
		}
	};
	/**
		Renders the template calling properties and renderers to 
		assamble the templates content.
	*/
	this.render = function () {
		var template 	= this;
		var buffer 		= this.content.replace(/%\{(.*?)\}/g, function () {
			var result = template.parseTokenAndOptions(arguments[1]);
			return template.renderToken(result.token, result.options);
		});
		
		return buffer;
	};
	/**
		Renders token found in the template.
	*/
	this.renderToken = function (token, options) {
		var content = '';
		var renderer;
		
		if (renderer = JSTemplate._renderers[token]) {
			// use renderer to render the tokens content
			content = renderer.render(options, this);
		}else if (this.properties[token]) {
			// substitute the token with a property
			content = this.properties[token];
		}
		
		return content;
	};
	/**
		Creates an object describing the token and options.
	*/
	this.parseTokenAndOptions = function (matchedToken) {
		var result 	= {token:'', options:{}};
		var parts 	= matchedToken.split(', ');
		
		if (parts.length >= 1) {
			// only a token, no options
			result.token = parts.shift();
		}
		
		if (parts.length > 0) {
			// take options
			for (var i = 0, c = parts.length; i < c; i++) {
				var s = parts[i];
				var t = s.split(':');
				
				if ( t.length == 2 ) {
					result.options[t[0]] = t[1];
				}
			}
		}
		
		return result;
	};
};

/**
	Collection of global renderers
*/
JSTemplate._renderers = {};
/**
	Sets a global renderer usable for every template object.
*/
JSTemplate.setRenderer = function (renderer) {
	
	var isRenderer = !!renderer.rendersToken && !!renderer.render;
	
	if (isRenderer) {
		var r = JSTemplate._renderers;
		var rendersToken = renderer.rendersToken;
		
		if (!r[rendersToken]) {
			r[rendersToken] = renderer;
		}else{
			console.warn( 'Already has renderer for token: ' + rendersToken );
		}
	}else{
		console.error( 'Argument is not a renderer' );
	}
};


/**
	Renderers
*/

// Each
JSTemplate.setRenderer( new (function () {
	this.rendersToken 	= 'each';
	this.render			= function (options, template) {
		// render content
		var content = '';
		var data 	= template.get( options['var'] );
		
		if (!data || !data.length) {
			var path = [template.name, options['var']].join('.');
			console.warn(path + ' did not provide data. Nothing rendered');
			return '';
		}
		
		var str	= options['repeatString'];
		
		for (var i = 0, c = data.length; i < c; i++) {
			var value = data[i];
			content += str.replace(/%value/g, value);
		}
		
		return content;
	};
}));
// Foreach
JSTemplate.setRenderer( new (function () {
	this.rendersToken 	= 'foreach';
	this.render			= function (options, template) {
		// render content
		var content = '';
		var data 	= template.get( options['var'] );
		
		if (!data) {
			var path = [template.name, options['var']].join('.');
			console.warn(path + ' did not provide data. Nothing rendered');
			return '';
		}
		
		var str	= options['repeatString'];
		
		for (var key in data) {
			var value = data[key];
			if ( typeof(value) != 'function' ) {
				content += str.replace(/%key/g, key).replace(/%value/g, value);
			}
		}
		
		return content;
	};
}));