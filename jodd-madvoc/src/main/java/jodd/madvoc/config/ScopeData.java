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

package jodd.madvoc.config;

/**
 * Holds IN and OUT information for single scope.
 */
public class ScopeData {

	public In[] in;
	public Out[] out;

	public static class In {
		public Class type;			// property type
		public String name;			// property name
		public String target;		// real property name, if different from 'name'

		/**
		 * Returns matched property name or <code>null</code> if name is not matched.
		 * <p>
		 * Matches if attribute name matches the required field name. If the match is positive,
		 * injection or outjection is performed on the field.
		 * <p>
		 * Parameter name matches field name if param name starts with field name and has
		 * either '.' or '[' after the field name.
		 * <p>
		 * Returns real property name, once when name is matched.
		 */
		public String matchedPropertyName(String value) {
			// match
			if (!value.startsWith(name)) {
				return null;
			}
			int requiredLen = name.length();
			if (value.length() >= requiredLen + 1) {
				char c = value.charAt(requiredLen);
				if ((c != '.') && (c != '[')) {
					return null;
				}
			}

			// get param
			if (target == null) {
				return value;
			}
			return target + value.substring(name.length());
		}


	}
	public static class Out {
		public Class type;			// property type
		public String name;			// property name
		public String target;		// real property name, if different from 'name'
	}

}
