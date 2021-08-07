<h1>System Settings</h1>

<div class="system-section datetime">
	<h2>Date &amp; Time</h2>

	<div id="dateTime">

		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">Timezone</div>
			<table>
				<tbody>
					<tr>
						<td><select name="timeZone" id="timeZone">
								<option value="Kwajalein" title="symbol">gmt-12_00_eniwetok_kwajalein</option>

								<option value="Pacific/Samoa" title="symbol">gmt-11_00_midway_island_samoa</option>
								<option value="US/Hawaii" title="symbol">gmt-10_00_hawaii</option>
								<option value="US/Alaska" title="symbol">gmt-09_00_alaska</option>
								<option value="America/Vancouver" title="symbol">gmt-08_00_las_vegas_san_francisco_vancouver</option>
								<option value="America/Denver" title="symbol">gmt-07_00_mountain_time_denver</option>
								<option value="US/Arizona" title="symbol">gmt-07_00_arizona</option>

								<option value="US/Central" title="symbol">gmt-0600_central_time_us_and_canada</option>
								<option value="America/Mexico_City" title="symbol">gmt-0600_mexico_city</option>
								<option value="Canada/Saskatchewan" title="symbol">gmt-0600_saskatchewan</option>
								<option value="America/New_York" title="symbol">gmt-05_00_eastern_time_new_york_toronto</option>
								<option value="America/Indiana/Indianapolis" title="symbol">gmt-05_00_bogota_lima_quito_indiana</option>
								<option value="America/Santiago" title="symbol">gmt-04_00_atlantic_time_canada_caracas_la_paz_santiago</option>

								<option value="Canada/Newfoundland" title="symbol">gmt-03_30_newfoundland</option>
								<option value="America/Buenos_Aires" title="symbol">gmt-03_00_brasilia_buenos_aires_georgetown_greenland</option>
								<option value="Canada/Atlantic" title="symbol">gmt-02_00_mid-atlantic</option>
								<option value="Atlantic/Azores" title="symbol">gmt-01_00_azores_cape_verde_is_</option>
								<option value="Europe/London" title="symbol">gmt_casablanca_greenwich_mean</option>
								<option value="Europe/Amsterdam" title="symbol">gmt_01_00_amsterdam_berlin_rome_stockholm_vienna_madrid_paris</option>

								<option value="Europe/Budapest" title="symbol">gmt_01_00_warsaw_budapest_bern</option>
								<option value="Europe/Helsinki" title="symbol">gmt_02_00_athens_helsinki_istanbul_riga</option>
								<option value="Africa/Cairo" title="symbol">gmt_02_00_cairo</option>
								<option value="Europe/Minsk" title="symbol">gmt_02_00_lebanon_minsk</option>
								<option value="Israel" title="symbol">gmt_02_00_israel</option>
								<option value="Europe/Moscow" title="symbol">gmt_03_00_baghdad_kuwait_riyadh_moscow_st_petersburg_nairobi</option>

								<option value="Asia/Kuwait" title="symbol">gmt_03_00_iraq</option>
								<option value="Iran" title="symbol">gmt_03_30_tehran</option>
								<option value="Asia/Yerevan" title="symbol">gmt_04_00_abu_dhabi_muscat_baku_tbilisi_yerevan</option>
								<option value="Asia/Kabul" title="symbol">gmt_04_30_kabul</option>
								<option value="Asia/Karachi" title="symbol">gmt_05_00_ekaterinburg_islamabad_karachi_tashkent</option>
								<option value="Asia/Calcutta" title="symbol">gmt_05_30_calcutta_chennai_mumbai_new_delhi</option>

								<option value="Asia/Kathmandu" title="symbol">gmt_05_45_kathmandu</option>
								<option value="Asia/Novosibirsk" title="symbol">gmt_06_00_almaty_novosibirsk_astana_dhaka_sri_jayawardenepura</option>
								<option value="Asia/Rangoon" title="symbol">gmt_06_30_rangoon</option>
								<option value="Asia/Bangkok" title="symbol">gmt_07_00_bangkok_hanoi_jakarta_krasnoyarsk</option>
								<option value="Hongkong" title="symbol">gmt_08_00_beijing_chongging_hong_kong_kuala_lumpur_singapore_taipei</option>
								<option value="Asia/Tokyo" title="symbol">gmt_09_00_osaka_sapporo_tokyo_seoul_yakutsk</option>

								<option value="Australia/Adelaide" title="symbol">gmt_09_30_adelaide_darwin</option>
								<option value="Australia/Brisbane" title="symbol">gmt_10_00_brisbane_canberra_melbourne_sydney_guam_vladivostok</option>
								<option value="Asia/Magadan" title="symbol">gmt_11_00_magadan_solomon_is_new_caledonia</option>
								<option value="Pacific/Auckland" title="symbol">gmt_12_00_aucklan_wellington_fiji_kamchatka_marshall_is_</option>
								<option value="Pacific/Samoa" title="symbol">gmt_13_00_nuku_alofa</option>
						</select></td>
					</tr>

				</tbody>
			</table>
		</div>


		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">
				<input id="manualTime" type="radio" name="useNtp" value="false">&nbsp;Manual
				time
			</div>
			<table>
				<tbody>
					<tr>
						<td><label>Date</label></td>
						<td><input type="text" name="dateString" id="manualDate"></td>
					</tr>
					<tr>
						<td><label>Time</label></td>
						<td><select id="timeHour" name="timeHour">
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
						</select> : <select id="timeMinute" name="timeMinute">
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
								<option value="25">25</option>
								<option value="26">26</option>
								<option value="27">27</option>
								<option value="28">28</option>
								<option value="29">29</option>
								<option value="30">30</option>
								<option value="31">31</option>
								<option value="32">32</option>
								<option value="33">33</option>
								<option value="34">34</option>
								<option value="35">35</option>
								<option value="36">36</option>
								<option value="37">37</option>
								<option value="38">38</option>
								<option value="39">39</option>
								<option value="40">40</option>
								<option value="41">41</option>
								<option value="42">42</option>
								<option value="43">43</option>
								<option value="44">44</option>
								<option value="45">45</option>
								<option value="46">46</option>
								<option value="47">47</option>
								<option value="48">48</option>
								<option value="49">49</option>
								<option value="50">50</option>
								<option value="51">51</option>
								<option value="52">52</option>
								<option value="53">53</option>
								<option value="54">54</option>
								<option value="55">55</option>
								<option value="56">56</option>
								<option value="57">57</option>
								<option value="58">58</option>
								<option value="59">59</option>
						</select></td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">
				<input id="dynamicTime" type="radio" name="useNtp" value="true">&nbsp;Automatic
			</div>
			<table>
				<tbody>
					<tr>
						<td><label>NTP server</label></td>
						<td><input type="text" id="ntpServer" name="ntpserver"
							value="0.pool.ntp.org"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

<%--
	<input type="button" value="Save Date &amp; Time Settings"
		onclick="SystemSettingsController.saveDateTimeSettings();">
--%>
</div>

<div id="network">
	<div class="system-section">
		<h2>Network</h2>

		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">
				<input id="dhcp" type="checkbox" name="dhcp" value="true"
					checked="checked">&nbsp;Use DHCP
			</div>
		</div>

		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">Identity</div>
			<table>
				<tbody>
					<tr>
						<td><label>Hostname</label></td>
						<td><input type="text" name="hostname" id="hostname" value="" /></td>
					</tr>
				</tbody>
			</table>

		</div>

		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">Static
				IP</div>

			<table>
				<tbody>
					<tr>
						<td><label>Address</label></td>
						<td><input type="text" name="address" id="address"></td>
					</tr>
					<tr>
						<td><label>Mask</label></td>
						<td><input type="text" name="mask" id="mask"></td>
					</tr>
					<tr>
						<td><label>Router</label></td>
						<td><input type="text" name="defaultRoute" id="defaultRoute"></td>
					</tr>
				</tbody>
			</table>

			<div class="section-content-title title-with-bottom-margin">
				<br>Nameservers
			</div>

			<table>
				<tbody>
					<tr>
						<td><label>Nameserver #1</label></td>
						<td><input type="text" name="nameServer1" id="nameServer1"></td>
					</tr>
					<tr>
						<td><label>Nameserver #2</label></td>
						<td><input type="text" name="nameServer2" id="nameServer2"></td>
					</tr>
					<tr>
						<td><label>Nameserver #3</label></td>
						<td><input type="text" name="nameServer3" id="nameServer3"></td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="section-content">
			<div class="section-content-title title-with-bottom-margin">Protocol</div>
			<table>
				<tbody>
					<tr>
						<td><label>Protocol</label></td>
						<td><select name="protocolDescriptor" id="protocolDescriptor">
								<option value="HTTP">HTTP</option>
								<option value="HTTPS">HTTPS</option>
								<option value="HTTP-HTTPS">HTTP &amp; HTTPS</option>
						</select></td>
					</tr>
					<tr>
						<td><label>HTTP port</label></td>
						<td><input type="text" name="httpPort" id="httpPort"
							value="8080"></td>
					</tr>
					<tr>
						<td><label>HTTPS port</label></td>
						<td><input type="text" name="httpsPort" id="httpsPort"
							value="8443"></td>
					</tr>

				</tbody>
			</table>
		</div>
	</div>

<%--
	<input type="button" value="Save Network Settings"
		onclick="SystemSettingsController.saveNetworkSettings();">
--%>
</div>


<div class="system-section">
	<h2>Preferences</h2>
	<div class="section-content">
		<table>
			<tbody>
				<tr>
					<td><label>Web Prefix</label></td>
					<td><input type="text" id="webPrefix" name="webPrefix"
						value="" /></td>
				</tr>
				<tr>
					<td><label>Connect Timeout</label></td>
					<td><input type="text" id="connectTimeout"
						name="connectTimeout" value="" /></td>
				</tr>
				<tr>
					<td><label>Free Space</label></td>
					<td><input type="text" id="freeSpace" name="freeSpace"
						value="" /></td>
				</tr>
				<tr>
					<td><label>Days of JPEG</label></td>
					<td><input type="text" id="daysJpeg" name="daysJpeg" value="" /></td>
				</tr>
				<tr>
					<td><label>Clean Rate</label></td>
					<td><input type="text" id="cleanRate" name="cleanRate"
						value="" /></td>
				</tr>
				<tr>
					<td><label>Phone Home Url</label></td>

					<td><input type="text" id="phonehomeUrl" name="phonehomeUrl"
						value="" /></td>
				</tr>
			</tbody>
		</table>
	</div>

	<input type="button" value="Save Preferences"
		onclick="SystemSettingsController.savePreferences();">
</div>

<div class="system-section">
	<h2>Certificates</h2>

	<div class="section-content">
		<input type="button" value="Manage certificates"
			name="Manage Certificates"
			onclick="location.assign('certificates');">
	</div>
</div>

<br>

<jsp:include page="/jsp/content/components/scripts.jsp" />

<!-- Loading action scripts -->
<script type="text/javascript" src="js/system-settings/templates.js"></script>
<script type="text/javascript" src="js/system-settings/builder.js"></script>
<script type="text/javascript" src="js/system-settings/helpers.js"></script>
<script type="text/javascript" src="js/system-settings/listener.js"></script>
<script type="text/javascript" src="js/system-settings/controller.js"></script>
<script type="text/javascript" src="js/system-settings/validators.js"></script>

<script type="text/javascript">
	//var now = new Date();
	//jQuery('#manualDate').val( now.getDate() + '-' + now.getMonth() + '-' + now.getFullYear() );
	//jQuery('#manualTime').val( now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds() );

	jQuery('#manualDate').datepicker({
		dateFormat : 'yy-mm-dd'
	});
</script>

<!-- Start loading the config -->
<script type="text/javascript">
	try {
		ConfigLoadJSON('systemsettingsgetjson');
	} catch (e) {
		console.error(e);
	}
</script>
