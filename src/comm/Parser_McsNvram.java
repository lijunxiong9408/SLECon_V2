package comm;
import comm.agent.AgentPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import static logic.util.SiteManagement.MON_MGR;


public class Parser_McsNvram {
    /**
     * The mapping table of MCS NVRAM address and index.
     */
    private static final short ADDR2IDX[] = {
		8, 248, 252, 256, 260, 264, 268, 272, 276, 280, 284, 288, 292, 296, 300, 304, 308, 312, 316, 320, 324, 328, 332, 336, 340, 344, 348, 352,
	    356, 360, 364, 368, 372, 376, 380, 384, 388, 392, 396, 400, 404, 408, 412, 416, 420, 424, 428, 432, 436, 440, 444, 448, 452, 456, 460,
	    464, 468, 472, 476, 480, 484, 488, 492, 496, 500, 504, 508, 512, 516, 520, 524, 528, 532, 536, 540, 544, 548, 552, 556, 560, 564, 568,
	    572, 576, 580, 584, 588, 592, 596, 600, 604, 608, 612, 616, 620, 624, 628, 632, 636, 640, 644, 648, 652, 656, 660, 664, 668, 672, 676,
	    680, 684, 688, 692, 696, 700, 704, 708, 712, 716, 720, 724, 728, 732, 736, 740, 744, 748, 752, 756, 760, 764, 768, 772, 776, 780, 784,
	    788, 792, 796, 800, 804, 808, 812, 816, 820, 824, 828, 832, 836, 840, 844, 848, 852, 856, 860, 864, 868, 872, 876, 880, 884, 888, 892,
	    896, 900, 904, 908, 912, 916, 920, 924, 928, 932, 936, 940, 944, 948, 952, 956, 960, 964, 968, 972, 976, 980, 984, 988, 992, 996, 1000,
	    1004, 1008, 1012, 1016, 1020, 1024, 1028, 1032, 1036, 1040, 1044, 1048, 1052, 1056, 1060, 1064, 1068, 1072, 1076, 1080, 1084, 1088, 1092,
	    1096, 1100, 1104, 1108, 1112, 1116, 1120, 1124, 1128, 1132, 1136, 1140, 1144, 1148, 1152, 1156, 1160, 1164, 1168, 1172, 1176, 1180, 1184,
	    1188, 1192, 1196, 1200, 1204, 1208, 1212, 1216, 1220, 1224, 1228, 1232, 1236, 1240, 1244, 1248, 1252, 1256, 1260, 1264, 1268, 1272, 1276,
	    1280, 1284, 1288, 1292, 1296, 1300, 1304, 1308, 1312, 1316, 1320, 1324, 1328, 1332, 1336, 1340, 1344, 1348, 1352, 1356, 1360, 1364, 1368,
	    1372, 1376, 1380, 1384, 1388, 1392, 1396, 1400, 1404, 1408, 1412, 1416, 1420, 1424, 1428, 1432, 1436, 1440, 1444, 1448, 1452, 1456, 1460,
	    1464, 1468, 1472, 1476, 1480, 1484, 1488, 1492, 1496, 1500, 1504, 1508, 1512, 1516, 1520, 1524, 1528, 1532, 1536, 1540, 1544, 1548, 1552,
	    1556, 1560, 1564, 1568, 1572, 1576, 1580, 1584, 1588, 1592, 1596, 1600, 1604, 1608, 1612, 1616, 1620, 1624, 1628, 1632, 1636, 1640, 1644,
	    1648, 1652, 1656, 1660, 1664, 1668, 1672, 1676, 1680, 1684, 1688, 1692, 1696, 1700, 1704, 1708, 1712, 1716, 1720, 1724, 1728, 1732, 1736,
	    1740, 1744, 1748, 1752, 1756, 1760, 1764, 1768, 1772, 1776, 1780, 1784, 1788, 1792, 1796, 1800, 1804, 1808, 1812, 1816, 1820, 1824, 1828,
	    1832, 1836, 1840, 1844, 1848, 1852, 1856, 1860, 1864, 1868, 1872, 1876, 1880, 1884, 1888, 1892, 1896, 1900, 1904, 1908, 1912, 1916, 1920,
	    1924, 1928, 1932, 1936, 1940, 1944, 1948, 1952, 1956, 1960, 1964, 1968, 1972, 1976, 1980, 1984, 1988, 1992, 1996, 2000, 2004, 2008, 2012,
	    2016, 2020, 2024, 2028, 2032, 2036, 2040, 2044, 2048, 2052, 2056, 2060, 2064, 2068, 2072, 2076, 2080, 2084, 2088, 2092, 2096, 2100, 2104,
	    2108, 2112, 2116, 2120, 2124, 2128, 2132, 2136, 2140, 2144, 2148, 2152, 2156, 2160, 2164, 2168, 2172, 2176, 2180, 2184, 2188, 2192, 2196,
	    2200, 2204, 2208, 2212, 2216, 2220, 2224, 2228, 2232, 2236, 2240, 2244, 2248, 2252, 2256, 2260, 2264, 2268, 2272, 2276, 2280, 2284, 2288,
	    2292, 2296, 2300, 2308, 2312, 2316, 2320, 2324, 2332, 2336, 2340, 2344, 2348, 2352, 2356, 2360, 2560, 2564, 2568, 2572, 2576, 2580, 2584,
	    2588, 2592, 2596, 2600, 2604, 2608, 2612, 2616, 2620, 2624, 2628, 2632, 2636, 2640, 2644, 2648, 2652, 2656, 2660, 2664, 2668, 2672, 2676,
	    2680, 2684, 2688, 2692, 2696, 2700, 2704, 2708, 2712, 2716, 2720, 2724, 2728, 2732, 2736, 2740, 2744, 2748, 2752, 2756, 2760, 2764, 2768,
	    2772, 2776, 2780, 2784, 2788, 2792, 2796, 2800, 2804, 2808, 2812, 4097, 4098, 4099, 4108, 4110, 4112, 4114, 4116, 4118, 4120, 4122, 4124,
	    4126, 4128, 4130, 4132, 4134, 4136, 4138, 4140, 4142, 4144, 4148, 4152, 4158, 4160, 4162, 4164, 4170, 4172, 4174, 4176, 4184, 4191, 4192,
	    4193, 4194, 4196, 4198, 4202, 4210, 4212, 4224, 6272, 6273, 6274, 6275, 6276, 6277, 6278, 6279, 6280, 6281, 6282, 6283, 6284, 6285, 6286,
	    6287, 6288, 6289, 6290, 6291, 6292, 6293, 6294, 6295, 6296, 6297, 6298, 6299, 6300, 6301, 6302, 6303, 6304, 6305, 6306, 6307, 6308, 6309, 6384, 6385, 6386, 6387, 6388,
	    6389, 6390, 6391, 6392, 6393, 6394, 6395, 6396, 6397, 6398, 6399, 6400, 6401, 6402, 6403, 6404, 6405, 6406, 6407, 6408, 6409, 6410, 6411,
	    6412, 6413, 6414, 6415, 6416, 6417, 6418, 6419, 6420, 6421, 6422, 6423, 6424, 6425, 6426, 6427, 6480, 6484, 6488, 6492, 6496, 6500, 6504,
	    6506, 6507 
    };

    /**
     * The mapping table of MCS NVRAM index and exactly position stored in shared memory.
     */
    private static final short ADDR2SHMPOS[] = {
		0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 88, 92, 96, 100, 104, 108, 112, 116, 120, 124, 128,
	    132, 136, 140, 144, 148, 152, 156, 160, 164, 168, 172, 176, 180, 184, 188, 192, 196, 200, 204, 208, 212, 216, 220, 224, 228, 232, 236,
	    240, 244, 248, 252, 256, 260, 264, 268, 272, 276, 280, 284, 288, 292, 296, 300, 304, 308, 312, 316, 320, 324, 328, 332, 336, 340, 344,
	    348, 352, 356, 360, 364, 368, 372, 376, 380, 384, 388, 392, 396, 400, 404, 408, 412, 416, 420, 424, 428, 432, 436, 440, 444, 448, 452,
	    456, 460, 464, 468, 472, 476, 480, 484, 488, 492, 496, 500, 504, 508, 512, 516, 520, 524, 528, 532, 536, 540, 544, 548, 552, 556, 560,
	    564, 568, 572, 576, 580, 584, 588, 592, 596, 600, 604, 608, 612, 616, 620, 624, 628, 632, 636, 640, 644, 648, 652, 656, 660, 664, 668,
	    672, 676, 680, 684, 688, 692, 696, 700, 704, 708, 712, 716, 720, 724, 728, 732, 736, 740, 744, 748, 752, 756, 760, 764, 768, 772, 776,
	    780, 784, 788, 792, 796, 800, 804, 808, 812, 816, 820, 824, 828, 832, 836, 840, 844, 848, 852, 856, 860, 864, 868, 872, 876, 880, 884,
	    888, 892, 896, 900, 904, 908, 912, 916, 920, 924, 928, 932, 936, 940, 944, 948, 952, 956, 960, 964, 968, 972, 976, 980, 984, 988, 992,
	    996, 1000, 1004, 1008, 1012, 1016, 1020, 1024, 1028, 1032, 1036, 1040, 1044, 1048, 1052, 1056, 1060, 1064, 1068, 1072, 1076, 1080, 1084,
	    1088, 1092, 1096, 1100, 1104, 1108, 1112, 1116, 1120, 1124, 1128, 1132, 1136, 1140, 1144, 1148, 1152, 1156, 1160, 1164, 1168, 1172, 1176,
	    1180, 1184, 1188, 1192, 1196, 1200, 1204, 1208, 1212, 1216, 1220, 1224, 1228, 1232, 1236, 1240, 1244, 1248, 1252, 1256, 1260, 1264, 1268,
	    1272, 1276, 1280, 1284, 1288, 1292, 1296, 1300, 1304, 1308, 1312, 1316, 1320, 1324, 1328, 1332, 1336, 1340, 1344, 1348, 1352, 1356, 1360,
	    1364, 1368, 1372, 1376, 1380, 1384, 1388, 1392, 1396, 1400, 1404, 1408, 1412, 1416, 1420, 1424, 1428, 1432, 1436, 1440, 1444, 1448, 1452,
	    1456, 1460, 1464, 1468, 1472, 1476, 1480, 1484, 1488, 1492, 1496, 1500, 1504, 1508, 1512, 1516, 1520, 1524, 1528, 1532, 1536, 1540, 1544,
	    1548, 1552, 1556, 1560, 1564, 1568, 1572, 1576, 1580, 1584, 1588, 1592, 1596, 1600, 1604, 1608, 1612, 1616, 1620, 1624, 1628, 1632, 1636,
	    1640, 1644, 1648, 1652, 1656, 1660, 1664, 1668, 1672, 1676, 1680, 1684, 1688, 1692, 1696, 1700, 1704, 1708, 1712, 1716, 1720, 1724, 1728,
	    1732, 1736, 1740, 1744, 1748, 1752, 1756, 1760, 1764, 1768, 1772, 1776, 1780, 1784, 1788, 1792, 1796, 1800, 1804, 1808, 1812, 1816, 1820,
	    1824, 1828, 1832, 1836, 1840, 1844, 1848, 1852, 1856, 1860, 1864, 1868, 1872, 1876, 1880, 1884, 1888, 1892, 1896, 1900, 1904, 1908, 1912,
	    1916, 1920, 1924, 1928, 1932, 1936, 1940, 1944, 1948, 1952, 1956, 1960, 1964, 1968, 1972, 1976, 1980, 1984, 1988, 1992, 1996, 2000, 2004,
	    2008, 2012, 2016, 2020, 2024, 2028, 2032, 2036, 2040, 2044, 2048, 2052, 2056, 2060, 2064, 2068, 2072, 2076, 2080, 2081, 2085, 2089, 2093,
	    2097, 2101, 2105, 2109, 2113, 2117, 2121, 2125, 2129, 2133, 2137, 2141, 2145, 2149, 2153, 2157, 2161, 2165, 2169, 2173, 2177, 2181, 2185,
	    2189, 2193, 2197, 2201, 2205, 2209, 2213, 2217, 2221, 2225, 2229, 2233, 2237, 2241, 2245, 2249, 2253, 2257, 2261, 2265, 2269, 2273, 2277,
	    2281, 2285, 2289, 2293, 2297, 2301, 2305, 2309, 2313, 2317, 2321, 2325, 2329, 2333, 2337, 2341, 2345, 2349, 2353, 2357, 2361, 2365, 2366,
	    2367, 2368, 2370, 2372, 2374, 2376, 2378, 2380, 2382, 2384, 2386, 2388, 2390, 2392, 2394, 2396, 2398, 2400, 2402, 2404, 2408, 2412, 2416,
	    2418, 2419, 2421, 2423, 2425, 2427, 2428, 2430, 2434, 2435, 2436, 2437, 2438, 2440, 2442, 2444, 2446, 2448, 2450, 2452, 2453, 2454, 2455,
	    2456, 2457, 2458, 2459, 2460, 2461, 2462, 2463, 2464, 2465, 2466, 2467, 2468, 2469, 2470, 2471, 2472, 2473, 2474, 2475, 2476, 2477, 2478,
	    2479, 2480, 2481, 2482, 2483, 2484, 2485, 2486, 2487, 2488, 2489, 2490, 2491, 2492, 2493, 2494, 2495, 2496, 2497, 2498, 2499, 2500, 2501,
	    2502, 2503, 2504, 2505, 2506, 2507, 2508, 2509, 2510, 2511, 2512, 2513, 2514, 2515, 2516, 2517, 2518, 2519, 2520, 2521, 2522, 2523, 2524,
	    2525, 2526, 2527, 2531, 2535, 2539, 2543, 2547, 2551, 2555, 2557, 2558
    };

    /**
     * The mapping table of MCS NVRAM index and data size.
     */
    private static final byte SIZE[] = {
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 2, 2, 2,
	    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 2, 1, 2, 2, 2, 2, 1, 2, 4, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 2, 1, 1
    };


    /**
     * The instance of {@code Agent}.
     */
    private final Agent agent;

    /**
     * The reference to MCS NVRAM data.
     */
    private final byte[] mcs_nvram;


    /**
     * MCS NVRAM parser.
     * @param hostname  It specifies the host name of {@code Agent}.
     * @param port      It specifies the host port of {@code Agent}.
     */
    public Parser_McsNvram ( String hostname, int port ) {
        this.agent     = MON_MGR.getAgent( hostname, port );
        this.mcs_nvram = this.agent.mcs_nvram;
    }


    /**
     * Get value in MCS NVRAM array.
     * @param pos   It specifies the starting position in MCS NVRAM array.
     * @param len   It specifies the number of array elements to be copied.
     * @return Returns a new array containing the specified range from the MCS NVRAM array.
     */
    public byte[] getMcsNvram ( int pos, int len ) {
        synchronized ( this.mcs_nvram ) {
            return Arrays.copyOfRange( this.mcs_nvram, Agent.MCS_NVRAM_ADDR_COMMIT_WORD + pos, Agent.MCS_NVRAM_ADDR_COMMIT_WORD + pos + len );
        }
    }


    /**
     * Update value in MCS NVRAM array.
     * @param pos   It specifies the starting position in {@code dat}.
     * @param dat   It specifies the byte array clones to MCS NVRAM array.
     * @param len   It specifies the number of array elements to be copied.
     */
    private void setMcsNvram ( int pos, byte[] dat, int len ) {
        synchronized ( this.mcs_nvram ) {
            System.arraycopy( dat, 0, mcs_nvram, Agent.MCS_NVRAM_ADDR_COMMIT_WORD + pos, len );
        }
    }


    /**
     * Update MCS NVRAM commit word to specify the MCS NVRAM address is updated.
     * @param idx   It specifies the position of MCS NVRAM address located.
     */
    private void setMcsNvramCommitWord ( int idx ) {
        synchronized ( this.mcs_nvram ) {
            this.mcs_nvram[ Agent.MCS_NVRAM_ADDR_COMMIT_WORD - 1 ] |= 1 << 7;
            this.mcs_nvram[ idx / 8 ]                              |= 1 << idx % 8;
        }
    }


    /**
     * Get {@code unsigned int} type of MCS NVRAM data.
     * @param addr  It specifies the address of MCS NVRAM.
     * @return Returns integer value on success; otherwise, returns {@code null} once the agent doesn't exist or invalid {@code addr}.
     */
    public Integer getSignedInt ( short addr ) {
        byte b[] = this.get( addr );
        if ( null == b )
            return null;
        return ( b[ 3 ] & 0xFF ) << 24 | ( b[ 2 ] & 0xFF ) << 16 | ( b[ 1 ] & 0xFF ) << 8 | ( b[ 0 ] & 0xFF );
    }


    /**
     * Get {@code unsigned int} type of MCS NVRAM data.
     * @param addr  It specifies the address of MCS NVRAM.
     * @return Returns integer value on success; otherwise, returns {@code null} once the agent doesn't exist or invalid {@code addr}.
     */
    public Long getUnsignedInt ( short addr ) {
        byte b[] = this.get( addr );
        if ( null == b )
            return null;
        return ( ( long )( b[ 3 ] & 0xFF ) << 24 | ( b[ 2 ] & 0xFF ) << 16 | ( b[ 1 ] & 0xFF ) << 8 | ( b[ 0 ] & 0xFF ) ) & 0xFFFFFFFF;
    }


    /**
     * Get {@code float} type of MCS NVRAM data.
     * @param addr  It specifies the address of MCS NVRAM.
     * @return Returns float value on success; otherwise, returns {@code null} once the agent doesn't exist or invalid {@code addr}.
     */
    public Float getFloat ( short addr ) {
        byte b[] = this.get( addr );
        if ( null == b )
            return null;
        return Float.intBitsToFloat( ( b[ 3 ] & 0xFF ) << 24 | ( b[ 2 ] & 0xFF ) << 16 | ( b[ 1 ] & 0xFF ) << 8 | ( b[ 0 ] & 0xFF ) );
    }


    /**
     * Get {@code unsigned short} type of MCS NVRAM data.
     * @param addr  It specifies the address of MCS NVRAM.
     * @return Returns short value on success; otherwise, returns {@code null} once the agent doesn't exist or invalid {@code addr}.
     */
    public Long getUnsignedShort ( short addr ) {
        byte b[] = this.get( addr );
        if ( null == b )
            return null;
        return ( ( long )( ( b[ 1 ] & 0xFF ) << 8 | ( b[ 0 ] & 0xFF ) ) & 0xFFFF );
    }


    /**
     *  Get {@code byte} type of MCS NVRAM data.
     *  @param addr  It specifies the address of MCS NVRAM.
     *  @return Returns byte value on success; otherwise, returns {@code null} once the agent doesn't exist or invalid {@code addr}.
     */
    public Long getUnsignedByte ( short addr ) {
        byte b[] = this.get( addr );
        if ( null == b )
            return null;
        return ( ( long )b[ 0 ] ) & 0xFF;
    }


    /**
     *  Set {@code int} type of MCS NVRAM data.
     *  @param addr  It specifies the address of MCS NVRAM.
     *  @param dat   It specifies the value.
     */
    public void setInt ( short addr, int dat ) {
        ByteBuffer buf = ByteBuffer.allocate( 4 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putInt( dat );
        this.set( addr, buf.array() );
    }


    /**
     *  Set {@code float} type of MCS NVRAM data.
     *  @param addr  It specifies the address of MCS NVRAM.
     *  @param dat   It specifies the value.
     */
    public void setFloat ( short addr, float dat ) {
        ByteBuffer buf = ByteBuffer.allocate( 4 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putFloat( dat );
        this.set( addr, buf.array() );
    }


    /**
     *  Set {@code short} type of MCS NVRAM data.
     *  @param addr  It specifies the address of MCS NVRAM.
     *  @param dat   It specifies the value.
     */
    public void setShort ( short addr, short dat ) {
        ByteBuffer buf = ByteBuffer.allocate( 2 );
        buf.order( ByteOrder.LITTLE_ENDIAN );
        buf.putShort( dat );
        this.set( addr, buf.array() );
    }


    /**
     *  Set {@code byte} type of MCS NVRAM data.
     *  @param addr  It specifies the address of MCS NVRAM.
     *  @param dat   It specifies the value.
     */
    public void setByte ( short addr, byte dat ) {
        this.set( addr, new byte[]{ dat } );
    }


    /**
     * Get MCS NVRAM address data.
     * @param addr  It specifies the address of MCS NVRAM.
     * @return Returns data in byte array on success; otherwise,
     *         returns {@code null} if the {@code addr} is invalid or undefined {@code Agent}.
     */
    private byte[] get ( short addr ) {
        int idx = Arrays.binarySearch( ADDR2IDX, 0, ADDR2IDX.length, addr );
        if ( this.agent == null || idx < 0 ) {
        	System.out.println("Can not find : "+addr);
        	return null;
        }
        return this.getMcsNvram( ADDR2SHMPOS[ idx ], SIZE[ idx ] );
    }


    /**
     * Set MCS NVRAM address data.
     * @param addr  It specifies the address of MCS NVRAM.
     * @param dat   It specifies the value.
     */
    private void set ( short addr, byte[] dat ) {
        int idx = Arrays.binarySearch( ADDR2IDX, 0, ADDR2IDX.length, addr );
        if ( this.agent == null || idx < 0 )
            return;
        this.setMcsNvram( ADDR2SHMPOS[ idx ], dat, dat.length );
        this.setMcsNvramCommitWord( idx );
    }


    /**
     * Send local copy of MCS NVRAM to OCS.
     */
    public void commit () {
        byte[] ret = new byte[ mcs_nvram.length + 1 ];
        synchronized ( mcs_nvram ) {
            System.arraycopy( mcs_nvram, 0, ret, 1, mcs_nvram.length );
        }
        ret[ 0 ] = AgentPacket.PACKET_MCSNVRAM;
        this.agent.send( ret );
    }
}
