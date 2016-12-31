// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.mail;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RFC2822AddressParserTest {

	@Test
	public void testEmailAddress() {
		RFC2822AddressParser.ParsedAddress address = new RFC2822AddressParser().parse("igor@jodd.org");

		assertEquals(null, address.getPersonalName());
		assertEquals("igor", address.getLocalPart());
		assertEquals("jodd.org", address.getDomain());
		assertTrue(address.isValid());

		address = new RFC2822AddressParser().parse("Vladimir <djs@gmail.com>");

		assertEquals("Vladimir", address.getPersonalName());
		assertEquals("djs", address.getLocalPart());
		assertEquals("gmail.com", address.getDomain());

		assertTrue(address.isValid());
	}

	@Test
	public void testValidEmails() {
		assertTrue(new RFC2822AddressParser().parse("bob @example.com").isValid());
		assertTrue(new RFC2822AddressParser().parse("\"bob\"  @  example.com").isValid());
		assertTrue(new RFC2822AddressParser().parse("\"bob\" (hi) @  example.com").isValid());
		assertTrue(new RFC2822AddressParser().parse("name.surname@example.com").isValid());

		assertTrue(new RFC2822AddressParser().parse("devnull@onyxbits.de").isValid());
		assertTrue(new RFC2822AddressParser().parse("< devnull @ onyxbits.de >").isValid());
		assertTrue(new RFC2822AddressParser().parse("<devnull@onyxbits.de>").isValid());
		assertFalse(new RFC2822AddressParser().parse("Patrick devnull@onyxbits.de").isValid());
		assertTrue(new RFC2822AddressParser().parse("Patrick <devnull@onyxbits.de>").isValid());
		assertTrue(new RFC2822AddressParser().parse("Patrickdevnull@onyxbits.de").isValid());
		assertFalse(new RFC2822AddressParser().parse("\"Patrick Ahlbrecht\" devnull@onyxbits.de").isValid());
		assertTrue(new RFC2822AddressParser().parse("\"Patrick Ahlbrecht\" <devnull@onyxbits.de>").isValid());
		assertTrue(new RFC2822AddressParser().parse("Patrick Ahlbrecht <devnull@onyxbits.de>").isValid());

		assertFalse(new RFC2822AddressParser().parse("Kayaks.org <kayaks@kayaks.org>").isValid());
		assertTrue(new RFC2822AddressParser().parse("\"Kayaks.org\" <kayaks@kayaks.org>").isValid());

		assertFalse(new RFC2822AddressParser().parse("[Kayaks] <kayaks@kayaks.org>").isValid());
		assertTrue(new RFC2822AddressParser().parse("\"[Kayaks]\" <kayaks@kayaks.org>").isValid());
	}

	@Test
	public void testReturnPath() {
		assertTrue(new RFC2822AddressParser().parse("\"[Kayaks]\" <kayaks@kayaks.org>").isValid());
		assertFalse(new RFC2822AddressParser().parse("\"[Kayaks]\" <kayaks@kayaks.org>").isValidReturnPath());

		assertTrue(new RFC2822AddressParser().parse("<kayaks@kayaks.org>").isValid());
		assertTrue(new RFC2822AddressParser().parse("<kayaks@kayaks.org>").isValidReturnPath());
	}

	@Test
	public void testCommentAsName() {
		RFC2822AddressParser.ParsedAddress address = new RFC2822AddressParser().parse("<bob@example.com> (Bob Smith)");
		assertEquals("Bob Smith", address.getPersonalName());

		address = new RFC2822AddressParser().parse("\"bob smith\" <bob@example.com> (Bobby)");
		assertEquals("bob smith", address.getPersonalName());

		address = new RFC2822AddressParser().parse("<bob@example.com> (Bobby)");
		assertEquals("Bobby", address.getPersonalName());

		address = new RFC2822AddressParser().parse("bob@example.com (Bobby)");
		assertEquals("Bobby", address.getPersonalName());

		address = new RFC2822AddressParser().parse("bob@example.com (Bob) (Smith)");
		assertEquals("Bob", address.getPersonalName());
	}

	@Test
	public void testValidEmails2() {
		assertEmail("me@example.com", true);
		assertEmail("a.nonymous@example.com", true);
		assertEmail("name+tag@example.com", true);
		//assertEmail("!#$%&'+-/=.?^`{|}~@[1.0.0.127]", true);
		//assertEmail("!#$%&'+-/=.?^`{|}~@[IPv6:0123:4567:89AB:CDEF:0123:4567:89AB:CDEF]", true);
		assertEmail("me(this is a comment)@example.com", true); // comments are discouraged but not prohibited by RFC2822.
		//assertEmail("me.example@com", true);
		assertEmail("309d4696df38ff12c023600e3bc2bd4b@fakedomain.com", true);
		assertEmail("ewiuhdghiufduhdvjhbajbkerwukhgjhvxbhvbsejskuadukfhgskjebf@gmail.net", true);

		assertEmail("NotAnEmail", false);
		assertEmail("me@", false);
		assertEmail("@example.com", false);
		assertEmail(".me@example.com", false);
		assertEmail("me@example..com", false);
		assertEmail("me\\@example.com", false);
	}

	private static void assertEmail(String emailaddress, boolean expected) {
		final boolean isValid = new RFC2822AddressParser().parse(emailaddress).isValid();
		if (isValid != expected) {
			throw new IllegalArgumentException(String.format("%s (expected: %s, but was: %s)", emailaddress, expected, isValid));
		}
	}
}