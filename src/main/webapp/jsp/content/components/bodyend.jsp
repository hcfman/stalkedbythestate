</div>

<script type="text/javascript">
	var chunks = location.pathname.split('/');
	var mm_id 	= 'mm_' + chunks.pop().replace(/\.jsp$/, '').replace(' ', '-');
	var mm_e	= document.getElementById(mm_id);
	
	if (mm_e) {
		jQuery(mm_e).addClass('active');
	}else{
		jQuery('#mm_system').addClass('active');
	}
</script>

</body>
</html>
