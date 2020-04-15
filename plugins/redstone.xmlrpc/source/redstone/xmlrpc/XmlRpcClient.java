/**
 *   nTorrent - A GUI client to administer a rtorrent process 
 *   over a network connection.
 *   
 *   Copyright (C) 2007  Kim Eik
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package redstone.xmlrpc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class XmlRpcClient extends XmlRpcParser implements XmlRpcInvocationHandler {
		
    /** The parsed value returned in a response. */
    protected Object returnValue;

    /** Writer to which XML-RPC messages are serialized. */
    protected StringWriter writer;
    
    /** Indicates whether or not the incoming response is a fault response. */
    protected boolean isFaultResponse;
    
    /** The serializer used to serialize arguments. */
    protected XmlRpcSerializer serializer = new XmlRpcSerializer();

    private Map<String,String> deprecatedMethodNamesMap;

	private List<String> commandsList;

	private Map<String,String> getDeprecatedMethodNamesMap () {
		if (deprecatedMethodNamesMap == null) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("encoding_list", "encoding.add");
			map.put("key_layout", "keys.layout.set");
			map.put("execute", "execute2");
			map.put("execute_throw", "execute.throw");
			map.put("execute_nothrow", "execute.nothrow");
			map.put("execute_capture", "execute.capture");
			map.put("execute_capture_nothrow", "execute.capture_nothrow");
			map.put("execute_raw", "execute.raw");
			map.put("execute_raw_nothrow", "execute.raw_nothrow");
			map.put("schedule", "schedule2");
			map.put("schedule_remove", "schedule_remove2");
			map.put("get_directory", "directory.default");
			map.put("directory", "directory.default.set");
			map.put("set_directory", "directory.default.set");
			map.put("ratio.disable", "group.seeding.ratio.disable");
			map.put("ratio.enable", "group.seeding.ratio.enable");
			map.put("ratio.max", "group2.seeding.ratio.max");
			map.put("ratio.max.set", "group2.seeding.ratio.max.set");
			map.put("ratio.min", "group2.seeding.ratio.min");
			map.put("ratio.min.set", "group2.seeding.ratio.min.set");
			map.put("ratio.upload", "group2.seeding.ratio.upload");
			map.put("ratio.upload.set", "group2.seeding.ratio.upload.set");
			map.put("to_date", "convert.date");
			map.put("to_elapsed_time", "convert.elapsed_time");
			map.put("to_gm_date", "convert.gm_date");
			map.put("to_gm_time", "convert.gm_time");
			map.put("to_kb", "convert.kb");
			map.put("to_mb", "convert.mb");
			map.put("to_time", "convert.time");
			map.put("to_throttle", "convert.throttle");
			map.put("to_xb", "convert.xb");
			map.put("get_connection_leech", "protocol.connection.leech");
			map.put("connection_leech", "protocol.connection.leech.set");
			map.put("set_connection_leech", "protocol.connection.leech.set");
			map.put("get_connection_seed", "protocol.connection.seed");
			map.put("connection_seed", "protocol.connection.seed.set");
			map.put("set_connection_seed", "protocol.connection.seed.set");
			map.put("encryption", "protocol.encryption.set");
			map.put("get_peer_exchange", "protocol.pex");
			map.put("peer_exchange", "protocol.pex.set");
			map.put("set_peer_exchange", "protocol.pex.set");
			map.put("get_memory_usage", "pieces.memory.current");
			map.put("get_max_memory_usage", "pieces.memory.max");
			map.put("max_memory_usage", "pieces.memory.max.set");
			map.put("set_max_memory_usage", "pieces.memory.max.set");
			map.put("get_check_hash", "pieces.hash.on_completion");
			map.put("check_hash", "pieces.hash.on_completion.set");
			map.put("set_check_hash", "pieces.hash.on_completion.set");
			map.put("get_preload_type", "pieces.preload.type");
			map.put("get_preload_min_size", "pieces.preload.min_size");
			map.put("set_preload_min_size", "pieces.preload.min_size.set");
			map.put("get_preload_required_rate", "pieces.preload.min_rate");
			map.put("set_preload_required_rate", "pieces.preload.min_rate.set");
			map.put("set_preload_type", "pieces.preload.type.set");
			map.put("get_stats_preloaded", "pieces.stats_preloaded");
			map.put("get_stats_not_preloaded", "pieces.stats_not_preloaded");
			map.put("get_safe_sync", "pieces.sync.always_safe");
			map.put("set_safe_sync", "pieces.sync.always_safe.set");
			map.put("get_timeout_sync", "pieces.sync.timeout");
			map.put("set_timeout_sync", "pieces.sync.timeout.set");
			map.put("get_timeout_safe_sync", "pieces.sync.timeout_safe");
			map.put("set_timeout_safe_sync", "pieces.sync.timeout_safe.set");
			map.put("throttle_down", "throttle.down");
			map.put("get_throttle_down_max", "throttle.down.max");
			map.put("get_throttle_down_rate", "throttle.down.rate");
			map.put("get_download_rate", "throttle.global_down.max_rate");
			map.put("set_download_rate", "throttle.global_down.max_rate.set");
			map.put("download_rate", "throttle.global_down.max_rate.set_kb");
			map.put("get_down_rate", "throttle.global_down.rate");
			map.put("get_down_total", "throttle.global_down.total");
			map.put("get_upload_rate", "throttle.global_up.max_rate");
			map.put("set_upload_rate", "throttle.global_up.max_rate.set");
			map.put("upload_rate", "throttle.global_up.max_rate.set_kb");
			map.put("get_up_rate", "throttle.global_up.rate");
			map.put("get_up_total", "throttle.global_up.total");
			map.put("throttle_ip", "throttle.ip");
			map.put("max_downloads", "throttle.max_downloads.set");
			map.put("get_max_downloads_div", "throttle.max_downloads.div");
			map.put("max_downloads_div", "throttle.max_downloads.div.set");
			map.put("set_max_downloads_div", "throttle.max_downloads.div.set");
			map.put("get_max_downloads_global", "throttle.max_downloads.global");
			map.put("max_downloads_global", "throttle.max_downloads.global.set");
			map.put("set_max_downloads_global", "throttle.max_downloads.global.set");
			map.put("get_max_peers", "throttle.max_peers.normal");
			map.put("max_peers", "throttle.max_peers.normal.set");
			map.put("set_max_peers", "throttle.max_peers.normal.set");
			map.put("get_max_peers_seed", "throttle.max_peers.seed");
			map.put("max_peers_seed", "throttle.max_peers.seed.set");
			map.put("set_max_peers_seed", "throttle.max_peers.seed.set");
			map.put("get_max_uploads", "throttle.max_uploads");
			map.put("max_uploads", "throttle.max_uploads.set");
			map.put("set_max_uploads", "throttle.max_uploads.set");
			map.put("get_max_uploads_div", "throttle.max_uploads.div");
			map.put("max_uploads_div", "throttle.max_uploads.div.set");
			map.put("set_max_uploads_div", "throttle.max_uploads.div.set");
			map.put("get_max_uploads_global", "throttle.max_uploads.global");
			map.put("max_uploads_global", "throttle.max_uploads.global.set");
			map.put("set_max_uploads_global", "throttle.max_uploads.global.set");
			map.put("min_downloads", "throttle.min_downloads.set");
			map.put("get_min_peers", "throttle.min_peers.normal");
			map.put("min_peers", "throttle.min_peers.normal.set");
			map.put("set_min_peers", "throttle.min_peers.normal.set");
			map.put("get_min_peers_seed", "throttle.min_peers.seed");
			map.put("min_peers_seed", "throttle.min_peers.seed.set");
			map.put("set_min_peers_seed", "throttle.min_peers.seed.set");
			map.put("min_uploads", "throttle.min_uploads.set");
			map.put("throttle_up", "throttle.up");
			map.put("get_throttle_up_max", "throttle.up.max");
			map.put("get_throttle_up_rate", "throttle.up.rate");
			map.put("dht_add_node", "dht.add_node");
			map.put("dht", "dht.mode.set");
			map.put("get_dht_port", "dht.port");
			map.put("dht_port", "dht.port.set");
			map.put("set_dht_port", "dht.port.set");
			map.put("get_dht_throttle", "dht.throttle.name");
			map.put("set_dht_throttle", "dht.throttle.name.set");
			map.put("dht_statistics", "dht.statistics");
			map.put("get_bind", "network.bind_address");
			map.put("bind", "network.bind_address.set");
			map.put("set_bind", "network.bind_address.set");
			map.put("get_ip", "network.local_address");
			map.put("ip", "network.local_address.set");
			map.put("set_ip", "network.local_address.set");
			map.put("get_http_capath", "network.http.capath");
			map.put("http_capath", "network.http.capath.set");
			map.put("set_http_capath", "network.http.capath.set");
			map.put("get_http_cacert", "network.http.cacert");
			map.put("http_cacert", "network.http.cacert.set");
			map.put("set_http_cacert", "network.http.cacert.set");
			map.put("get_max_open_http", "network.http.max_open");
			map.put("set_max_open_http", "network.http.max_open.set");
			map.put("get_http_proxy", "network.http.proxy_address");
			map.put("http_proxy", "network.http.proxy_address.set");
			map.put("set_http_proxy", "network.http.proxy_address.set");
			map.put("get_max_open_files", "network.max_open_files");
			map.put("set_max_open_files", "network.max_open_files.set");
			map.put("get_max_open_sockets", "network.max_open_sockets");
			map.put("get_port_open", "network.port_open");
			map.put("port_open", "network.port_open.set");
			map.put("set_port_open", "network.port_open.set");
			map.put("get_port_random", "network.port_random");
			map.put("port_random", "network.port_random.set");
			map.put("set_port_random", "network.port_random.set");
			map.put("get_port_range", "network.port_range");
			map.put("port_range", "network.port_range.set");
			map.put("set_port_range", "network.port_range.set");
			map.put("get_proxy_address", "network.proxy_address");
			map.put("proxy_address", "network.proxy_address.set");
			map.put("set_proxy_address", "network.proxy_address.set");
			map.put("get_receive_buffer_size", "network.receive_buffer.size");
			map.put("set_receive_buffer_size", "network.receive_buffer.size.set");
			map.put("set_scgi_dont_route", "network.scgi.dont_route.set");
			map.put("get_scgi_dont_route", "network.scgi.dont_route");
			map.put("scgi_local", "network.scgi.open_local");
			map.put("scgi_port", "network.scgi.open_port");
			map.put("get_send_buffer_size", "network.send_buffer.size");
			map.put("set_send_buffer_size", "network.send_buffer.size.set");
			map.put("set_xmlrpc_dialect", "network.xmlrpc.dialect.set");
			map.put("xmlrpc_dialect", "network.xmlrpc.dialect.set");
			map.put("get_xmlrpc_size_limit", "network.xmlrpc.size_limit");
			map.put("set_xmlrpc_size_limit", "network.xmlrpc.size_limit.set");
			map.put("xmlrpc_size_limit", "network.xmlrpc.size_limit.set");
			map.put("get_session", "session.path");
			map.put("set_session", "session.path.set");
			map.put("session", "session.path.set");
			map.put("get_name", "session.name");
			map.put("set_name", "session.name.set");
			map.put("get_session_on_completion", "session.on_completion");
			map.put("set_session_on_completion", "session.on_completion.set");
			map.put("session_save", "session.save");
			map.put("get_session_lock", "session.use_lock");
			map.put("set_session_lock", "session.use_lock.set");
			map.put("system.method.insert", "method.insert");
			map.put("system.method.erase", "method.erase");
			map.put("system.method.get", "method.get");
			map.put("system.method.set", "method.set");
			map.put("system.method.list_keys", "method.list_keys");
			map.put("system.method.has_key", "method.has_key");
			map.put("system.method.set_key", "method.set_key");
			map.put("system.file_allocate", "system.file.allocate");
			map.put("system.file_allocate.set", "system.file.allocate.set");
			map.put("get_max_file_size", "system.file.max_size");
			map.put("set_max_file_size", "system.file.max_size.set");
			map.put("get_split_file_size", "system.file.split_size");
			map.put("set_split_file_size", "system.file.split_size.set");
			map.put("get_split_suffix", "system.file.split_suffix");
			map.put("set_split_suffix", "system.file.split_suffix.set");
			map.put("load", "load.normal");
			map.put("load_start", "load.start");
			map.put("load_start_verbose", "load.start_verbose");
			map.put("load_raw", "load.raw");
			map.put("load_raw_start", "load.raw_start");
			map.put("load_raw_verbose", "load.raw_verbose");
			map.put("load_verbose", "load.verbose");
			map.put("get_tracker_numwant", "trackers.numwant");
			map.put("set_tracker_numwant", "trackers.numwant.set");
			map.put("tracker_numwant", "trackers.numwant.set");
			map.put("get_use_udp_trackers", "trackers.use_udp");
			map.put("set_use_udp_trackers", "trackers.use_udp.set");
			map.put("use_udp_trackers", "trackers.use_udp.set");
			map.put("d.get_base_filename", "d.base_filename");
			map.put("d.get_base_path", "d.base_path");
			map.put("d.get_bitfield", "d.bitfield");
			map.put("d.get_bytes_done", "d.bytes_done");
			map.put("d.get_chunk_size", "d.chunk_size");
			map.put("d.get_chunks_hashed", "d.chunks_hashed");
			map.put("d.get_complete", "d.complete");
			map.put("d.get_completed_bytes", "d.completed_bytes");
			map.put("d.get_completed_chunks", "d.completed_chunks");
			map.put("d.get_connection_current", "d.connection_current");
			map.put("d.set_connection_current", "d.connection_current.set");
			map.put("d.get_connection_leech", "d.connection_leech");
			map.put("d.get_connection_seed", "d.connection_seed");
			map.put("d.get_custom", "d.custom");
			map.put("d.set_custom", "d.custom.set");
			map.put("d.get_custom1", "d.custom1");
			map.put("d.set_custom1", "d.custom1.set");
			map.put("d.get_custom2", "d.custom2");
			map.put("d.set_custom2", "d.custom2.set");
			map.put("d.get_custom3", "d.custom3");
			map.put("d.set_custom3", "d.custom3.set");
			map.put("d.get_custom4", "d.custom4");
			map.put("d.set_custom4", "d.custom4.set");
			map.put("d.get_custom5", "d.custom5");
			map.put("d.set_custom5", "d.custom5.set");
			map.put("d.get_custom_throw", "d.custom_throw");
			map.put("create_link", "d.create_link");
			map.put("d.get_creation_date", "d.creation_date");
			map.put("delete_link", "d.delete_link");
			map.put("delete_tied", "d.delete_tied");
			map.put("d.get_directory", "d.directory");
			map.put("d.set_directory", "d.directory.set");
			map.put("d.get_directory_base", "d.directory_base");
			map.put("d.set_directory_base", "d.directory_base.set");
			map.put("d.get_down_rate", "d.down.rate");
			map.put("d.get_down_total", "d.down.total");
			map.put("d.get_free_diskspace", "d.free_diskspace");
			map.put("d.get_hash", "d.hash");
			map.put("d.get_hashing", "d.hashing");
			map.put("d.get_hashing_failed", "d.hashing_failed");
			map.put("d.set_hashing_failed", "d.hashing_failed.set");
			map.put("d.get_ignore_commands", "d.ignore_commands");
			map.put("d.set_ignore_commands", "d.ignore_commands.set");
			map.put("d.get_left_bytes", "d.left_bytes");
			map.put("d.get_local_id", "d.local_id");
			map.put("d.get_local_id_html", "d.local_id_html");
			map.put("d.get_loaded_file", "d.loaded_file");
			map.put("d.get_max_file_size", "d.max_file_size");
			map.put("d.set_max_file_size", "d.max_file_size.set");
			map.put("d.get_max_size_pex", "d.max_size_pex");
			map.put("d.get_message", "d.message");
			map.put("d.set_message", "d.message.set");
			map.put("d.get_mode", "d.mode");
			map.put("d.multicall", "d.multicall2");
			map.put("d.get_name", "d.name");
			map.put("d.get_peer_exchange", "d.peer_exchange");
			map.put("d.get_peers_accounted", "d.peers_accounted");
			map.put("d.get_peers_complete", "d.peers_complete");
			map.put("d.get_peers_connected", "d.peers_connected");
			map.put("d.get_peers_max", "d.peers_max");
			map.put("d.set_peers_max", "d.peers_max.set");
			map.put("d.get_peers_min", "d.peers_min");
			map.put("d.set_peers_min", "d.peers_min.set");
			map.put("d.get_peers_not_connected", "d.peers_not_connected");
			map.put("d.get_priority", "d.priority");
			map.put("d.set_priority", "d.priority.set");
			map.put("d.get_priority_str", "d.priority_str");
			map.put("d.get_ratio", "d.ratio");
			map.put("d.get_size_bytes", "d.size_bytes");
			map.put("d.get_size_chunks", "d.size_chunks");
			map.put("d.get_size_files", "d.size_files");
			map.put("d.get_size_pex", "d.size_pex");
			map.put("d.get_skip_rate", "d.skip.rate");
			map.put("d.get_skip_total", "d.skip.total");
			map.put("d.get_state", "d.state");
			map.put("d.get_state_changed", "d.state_changed");
			map.put("d.get_state_counter", "d.state_counter");
			map.put("d.get_throttle_name", "d.throttle_name");
			map.put("d.set_throttle_name", "d.throttle_name.set");
			map.put("d.get_tied_to_file", "d.tied_to_file");
			map.put("d.set_tied_to_file", "d.tied_to_file.set");
			map.put("d.get_tracker_focus", "d.tracker_focus");
			map.put("d.get_tracker_numwant", "d.tracker_numwant");
			map.put("d.set_tracker_numwant", "d.tracker_numwant.set");
			map.put("d.get_tracker_size", "d.tracker_size");
			map.put("d.get_up_rate", "d.up.rate");
			map.put("d.get_up_total", "d.up.total");
			map.put("d.get_uploads_max", "d.uploads_max");
			map.put("d.set_uploads_max", "d.uploads_max.set");
			map.put("t.get_group", "t.group");
			map.put("t.get_id", "t.id");
			map.put("t.set_enabled", "t.is_enabled.set");
			map.put("t.get_min_interval", "t.min_interval");
			map.put("t.get_normal_interval", "t.normal_interval");
			map.put("t.get_scrape_complete", "t.scrape_complete");
			map.put("t.get_scrape_downloaded", "t.scrape_downloaded");
			map.put("t.get_scrape_incomplete", "t.scrape_incomplete");
			map.put("t.get_scrape_time_last", "t.scrape_time_last");
			map.put("t.get_type", "t.type");
			map.put("t.get_url", "t.url");
			map.put("f.get_completed_chunks", "f.completed_chunks");
			map.put("f.get_frozen_path", "f.frozen_path");
			map.put("f.get_last_touched", "f.last_touched");
			map.put("f.get_match_depth_next", "f.match_depth_next");
			map.put("f.get_match_depth_prev", "f.match_depth_prev");
			map.put("f.get_offset", "f.offset");
			map.put("f.get_path", "f.path");
			map.put("f.get_path_components", "f.path_components");
			map.put("f.get_path_depth", "f.path_depth");
			map.put("f.get_priority", "f.priority");
			map.put("f.get_range_first", "f.range_first");
			map.put("f.get_range_second", "f.range_second");
			map.put("f.get_size_bytes", "f.size_bytes");
			map.put("f.get_size_chunks", "f.size_chunks");
			map.put("f.set_priority", "f.priority.set");
			map.put("fi.get_filename_last", "fi.filename_last");
			map.put("p.get_address", "p.address");
			map.put("p.get_client_version", "p.client_version");
			map.put("p.get_completed_percent", "p.completed_percent");
			map.put("p.get_down_rate", "p.down_rate");
			map.put("p.get_down_total", "p.down_total");
			map.put("p.get_id", "p.id");
			map.put("p.get_id_html", "p.id_html");
			map.put("p.get_options_str", "p.options_str");
			map.put("p.get_peer_rate", "p.peer_rate");
			map.put("p.get_peer_total", "p.peer_total");
			map.put("p.get_port", "p.port");
			map.put("p.get_up_rate", "p.up_rate");
			map.put("p.get_up_total", "p.up_total");
			map.put("view_add", "view.add");
			map.put("view_filter", "view.filter");
			map.put("view_filter_on", "view.filter_on");
			map.put("view_list", "view.list");
			map.put("view_set", "view.set");
			map.put("view_sort", "view.sort");
			map.put("view_sort_current", "view.sort_current");
			map.put("view_sort_new", "view.sort_new");
			deprecatedMethodNamesMap = map;
		}
		return deprecatedMethodNamesMap;
	}

	private List<String> getCommandsList() {
		if (commandsList == null) {
			List<String> list = new ArrayList<String>();
			list.add("encoding.add");
			list.add("keys.layout.set");
			list.add("execute2");
			list.add("execute.throw");
			list.add("execute.throw.bg");
			list.add("execute.nothrow");
			list.add("execute.nothrow.bg");
			list.add("execute.capture");
			list.add("execute.capture_nothrow");
			list.add("execute.raw");
			list.add("execute.raw.bg");
			list.add("execute.raw_nothrow");
			list.add("execute.raw_nothrow.bg");
			list.add("schedule2");
			list.add("schedule_remove2");
			list.add("directory.default.set");
			list.add("group.seeding.ratio.disable");
			list.add("group.seeding.ratio.enable");
			list.add("group2.seeding.ratio.max.set");
			list.add("group2.seeding.ratio.min.set");
			list.add("group2.seeding.ratio.upload.set");
			list.add("convert.date");
			list.add("convert.elapsed_time");
			list.add("convert.gm_date");
			list.add("convert.gm_time");
			list.add("convert.kb");
			list.add("convert.mb");
			list.add("convert.time");
			list.add("convert.throttle");
			list.add("convert.xb");
			list.add("protocol.connection.leech.set");
			list.add("protocol.connection.seed.set");
			list.add("protocol.encryption.set");
			list.add("protocol.pex.set");
			list.add("pieces.memory.max.set");
			list.add("pieces.hash.on_completion.set");
			list.add("pieces.preload.min_size.set");
			list.add("pieces.preload.min_rate.set");
			list.add("pieces.preload.type.set");
			list.add("pieces.sync.always_safe.set");
			list.add("pieces.sync.timeout.set");
			list.add("pieces.sync.timeout_safe.set");
			list.add("throttle.down.max");
			list.add("throttle.down.rate");
			//list.add("throttle.global_down.max_rate.set");
			list.add("throttle.global_down.max_rate.set_kb");
			//list.add("throttle.global_up.max_rate.set");
			list.add("throttle.global_up.max_rate.set_kb");
			list.add("throttle.max_downloads.set");
			list.add("throttle.max_downloads.div.set");
			list.add("throttle.max_downloads.global.set");
			list.add("throttle.max_peers.normal.set");
			list.add("throttle.max_peers.seed.set");
			list.add("throttle.max_uploads.set");
			list.add("throttle.max_uploads.div.set");
			list.add("throttle.max_uploads.global.set");
			list.add("throttle.min_peers.normal.set");
			list.add("throttle.min_peers.seed.set");
			list.add("dht.add_node");
			list.add("dht.mode.set");
			list.add("dht.port.set");
			list.add("dht.throttle.name.set");
			list.add("network.bind_address.set");
			list.add("network.local_address.set");
			list.add("network.http.capath.set");
			list.add("network.http.cacert.set");
			list.add("network.http.max_open.set");
			list.add("network.http.proxy_address.set");
			list.add("network.max_open_files.set");
			list.add("network.port_open.set");
			list.add("network.port_random.set");
			list.add("network.port_range.set");
			list.add("network.proxy_address.set");
			list.add("network.receive_buffer.size.set");
			list.add("network.scgi.dont_route.set");
			list.add("network.send_buffer.size.set");
			//list.add("network.xmlrpc.dialect.set");
			list.add("network.xmlrpc.size_limit.set");
			list.add("session.path.set");
			list.add("session.name.set");
			list.add("session.on_completion.set");
			list.add("session.use_lock.set");
			list.add("method.insert");
			list.add("method.erase");
			list.add("method.get");
			list.add("method.set");
			list.add("method.list_keys");
			list.add("method.has_key");
			list.add("method.set_key");
			list.add("system.file.allocate.set");
			list.add("system.file.max_size.set");
			list.add("system.file.split_size.set");
			list.add("system.file.split_suffix.set");
			list.add("system.daemon.set");
			list.add("system.<env>.set");
			list.add("system.shutdown.normal");
			list.add("system.shutdown.quick");
			//list.add("load.normal");
			//list.add("load.start");
			//list.add("load.start_verbose");
			//list.add("load.raw");
			//list.add("load.raw_start");
			//list.add("load.raw_verbose");
			//list.add("load.verbose");
			list.add("trackers.numwant.set");
			list.add("trackers.use_udp.set");
			list.add("d.check_hash");
			list.add("d.connection_current.set");
			list.add("d.custom.set");
			list.add("d.custom1.set");
			list.add("d.custom2.set");
			list.add("d.custom3.set");
			list.add("d.custom4.set");
			list.add("d.custom5.set");
			list.add("d.directory.set");
			list.add("d.directory_base.set");
			list.add("d.hashing_failed.set");
			list.add("d.ignore_commands.set");
			list.add("d.max_file_size.set");
			list.add("d.message.set");
			list.add("d.peers_max.set");
			list.add("d.peers_min.set");
			list.add("d.priority.set");
			list.add("d.start");
			list.add("d.stop");
			list.add("d.open");
			list.add("d.close");
			list.add("d.erase");
			list.add("d.is_open");
			list.add("d.name");
			list.add("d.directory");
			list.add("d.creation_date");
			list.add("d.tied_to_file");
			list.add("d.free_diskspace");
			list.add("d.throttle_name.set");
			list.add("d.tied_to_file.set");
			list.add("d.tracker_numwant.set");
			list.add("d.uploads_max.set");
			list.add("d.update_priorities");
			list.add("f.priority.set");
			list.add("t.is_enabled.set");
			list.add("view.add");
			list.add("view.filter");
			list.add("view.filter_on");
			list.add("view.list");
			list.add("view.set");
			list.add("view.sort");
			list.add("view.sort_current");
			list.add("view.sort_new");
			commandsList = list;
		}
		return commandsList;
	}

    public synchronized Object invoke(String method, List arguments ) throws Exception {
        beginCall( method );

        if (arguments != null){
            Iterator argIter = arguments.iterator();

            while ( argIter.hasNext() ){
	            writer.write( "<param>" );
	            serializer.serialize( argIter.next(), writer );
	            writer.write( "</param>" );
            }
        }

        endCall();
        return returnValue;
    }

    public synchronized Object invoke(String method, Object[] arguments ) throws XmlRpcException, XmlRpcFault{
        beginCall( method );

        if ( arguments != null ){
            for ( int i = 0; i < arguments.length; ++i ){
	            writer.write( "<param>" );
	            try {
					serializer.serialize( arguments[ i ], writer );
				} catch (IOException e) {
					throw new XmlRpcException(e.getMessage(),e);
				}
	            writer.write( "</param>" );
            }
        }
        
        endCall();
        
        return returnValue;
    }

    protected void beginCall( String methodName ) throws XmlRpcException{
            Map<String,String> deprecatedMethodNamesMap = getDeprecatedMethodNamesMap();
            boolean isDeprecated = deprecatedMethodNamesMap.containsKey(methodName);
            if (isDeprecated) {
                methodName = deprecatedMethodNamesMap.get(methodName);
            }
    		writer = new StringWriter( 2048 );
            writer.write( "<?xml version=\"1.0\" encoding=\"" );
            writer.write( XmlRpcMessages.getString( "XmlRpcClient.Encoding" ) );
            writer.write( "\"?>" );
            writer.write( "<methodCall><methodName>" );
            writer.write( methodName );
            writer.write( "</methodName><params>" );
            if (!getCommandsList().contains(methodName) && !methodName.endsWith("multicall")) {
                writer.write( "<param><value><string></string></value></param>" );
            }
    }

    protected void endCall() throws XmlRpcException, XmlRpcFault {
            writer.write( "</params>" );
            writer.write( "</methodCall>" );
    }

    protected void handleResponse(InputStream is) throws XmlRpcFault{
    	parse( new BufferedInputStream( is ) );
        
        if ( isFaultResponse ){
            XmlRpcStruct fault = ( XmlRpcStruct ) returnValue;
            isFaultResponse = false;
            
            throw new XmlRpcFault( fault.getInteger( "faultCode" ),
                                   fault.getString( "faultString" ) );
        }
    }    
     
    @Override
    public void startElement(String uri,String name,String qualifiedName,Attributes attributes )throws SAXException{
        if ( name.equals( "fault" ) ){
            isFaultResponse = true;
        }
        else{
            super.startElement( uri, name, qualifiedName, attributes );
        }
    }
    
    @Override
    protected void handleParsedValue(Object obj) {
    	returnValue = obj;
    }

}
