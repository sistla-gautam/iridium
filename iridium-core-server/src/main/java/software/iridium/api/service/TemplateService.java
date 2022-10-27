/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.service;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import software.iridium.api.util.SubdomainExtractor;

@Service
public class TemplateService {

  @Resource private SubdomainExtractor subdomainExtractor;
  @Resource private LoginDescriptorService loginDescriptorService;

  @Transactional(propagation = Propagation.SUPPORTS)
  public String describeIndex(
      final Model model,
      final HttpServletRequest servletRequest,
      final Map<String, String> params) {

    final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

    final var loginDescriptor = loginDescriptorService.getBySubdomain(subdomain);

    model.addAttribute("displayName", loginDescriptor.getDisplayName());
    model.addAttribute(
        "externalProviderDescriptors", loginDescriptor.getExternalProviderDescriptors());

    return "index";
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public String describeRegister(final Model model, final HttpServletRequest servletRequest) {
    final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

    final var loginDescriptor = loginDescriptorService.getBySubdomain(subdomain);

    model.addAttribute("displayName", loginDescriptor.getDisplayName());
    //        model.addAttribute("allowGithub", loginDescriptor.getAllowGithub());
    //        model.addAttribute("allowGoogle", loginDescriptor.getAllowGoogle());
    //        model.addAttribute("allowApple", loginDescriptor.getAllowApple());
    //        model.addAttribute("allowMicrosoft", loginDescriptor.getAllowMicrosoft());

    return "register";
  }
}