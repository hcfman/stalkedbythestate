SharedTemplates.setTemplate('mail-provider', '\
<tbody>\
<tr>\
<td class="label"><label>Name</label></td>\
<td><input type="text" name="name" value="%{name}"></td>\
</tr>\
<tr>\
<td class="label"><label>Description</label></td>\
<td><input type="text" name="description" value="%{description}"></td>\
</tr>\
<tr>\
<td class="label"><label>Mailhost</label></td>\
<td><input type="text" name="mailhost" value="%{mailhost}"></td>\
</tr>\
<tr>\
<td class="label"><label>Encryption type</td>\
<td>\
<select id="encType" name="encType">\
%{each, var:encryptionTypeNames, repeatString:<option value="%value">%value</options>}\
</select>\
</td>\
</tr>\
<tr>\
<td class="label"><label>Port</label></td>\
<td><input type="text" name="port" value="%{port}"></td>\
</tr>\
<tr>\
<td class="label"><label>Username</label></td>\
<td><input type="text" name="username" value="%{username}"></td>\
</tr>\
<tr>\
<td class="label"><label>Password</label></td>\
<td><input type="password" name="password" value="%{password}"></td>\
</tr>\
<tr>\
<td class="label"><label>From</label></td>\
<td><input type="text" name="from" value="%{from}"></td>\
</tr>\
</tbody>\
');