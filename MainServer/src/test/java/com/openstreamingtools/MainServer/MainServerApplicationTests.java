package com.openstreamingtools.MainServer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainServerApplicationTests {

	byte[] staticTestMessageBytes = new byte[]{
			(byte)0x61,
			(byte)0x69,(byte)0x72,(byte)0x44,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0x4a,(byte)0x1c,
			(byte)0x9b,(byte)0xba,(byte)0x88,(byte)0xb4,(byte)0xbe,(byte)0x19,(byte)0xa3,(byte)0xd1,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x00,(byte)0x53,
			(byte)0x00,(byte)0x4c,(byte)0x00,(byte)0x4a,(byte)0x00,(byte)0x53,(byte)0x00,(byte)0x00,(byte)0x00,
			(byte)0x22,(byte)0x00,(byte)0x44,(byte)0x00,(byte)0x49,(byte)0x00,(byte)0x53,(byte)0x00,(byte)0x43,(byte)0x00,
			(byte)0x4f,(byte)0x00,(byte)0x56,(byte)0x00,(byte)0x45,(byte)0x00,(byte)0x52,(byte)0x00,(byte)0x45,(byte)0x00,(byte)0x52,(byte)0x00,
			(byte)0x5f,(byte)0x00,(byte)0x48,(byte)0x00,(byte)0x4f,(byte)0x00,(byte)0x57,(byte)0x00,(byte)0x44,(byte)0x00,(byte)0x59,(byte)0x00,
			(byte)0x5f,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x16,(byte)0x00,(byte)0x73,(byte)0x00,(byte)0x74,(byte)0x00,(byte)0x61,(byte)0x00,
			(byte)0x67,(byte)0x00,(byte)0x65,(byte)0x00,(byte)0x6c,(byte)0x00,(byte)0x69,(byte)0x00,(byte)0x6e,(byte)0x00,(byte)0x71,(byte)0x00,
			(byte)0x6a,(byte)0x00,(byte)0x73,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x14,(byte)0x00,(byte)0x32,(byte)0x00,(byte)0x2e,
			(byte)0x00,(byte)0x30,(byte)0x00,(byte)0x2e,(byte)0x00,(byte)0x30,(byte)0x00,(byte)0x2d,(byte)0x00,(byte)0x42,(byte)0x00,
			(byte)0x65,(byte)0x00,(byte)0x74,(byte)0x00,(byte)0x61,(byte)0xea,(byte)0x60
	};


	@Test
	void contextLoads() {
	}

}
