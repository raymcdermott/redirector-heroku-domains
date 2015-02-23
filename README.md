# redirector-heroku-domains

A command line interface to add domains to an Heroku app so that it can support domain forwarding.

This is a helper script to domain-redirector [https://github.com/raymcdermott/domain-redirector]

## Details

A custom domain name (for example custom.foo.com) can be configured in DNS, using a CNAME to point to a Heroku app (for example foo-19876.herokuapp.com)

The Heroku app must be configured with the custom.foo.com domain so that the Heroku edge router can be assured that the configuration is valid.

More details on how custom domains work on the Heroku platform is here [https://devcenter.heroku.com/articles/custom-domains]

This program uses the Heroku REST API to configure domains on your applications based on routes stored in MongoDB (see the domain-redirector docs for more details on how that is achieved).

*By default the program will add **ALL** of the 'source' domains for all of the routes in the given collection.*

## Usage

redirector-heroku-domains

The program is controlled by configuration variables rather than command line options.

This makes it more portable and (assuming shellshock is fixed!) secure (cannot read env vars from ps listing for example!).

### Environment Variables

The application must be configured using the following variables / defaults

| Name             | Default Value |
| ----             | ------------- |
| HEROKU_API_KEY   | none |
| HEROKU_APP       | none |
| MONGO_URL        | mongodb://localhost/test |
| MONGO_COLLECTION | redirections |

## Operational / budget considerations

You can have all of the domains forwarded from one app or spread the forwarding across multiple apps.

This can be achieved by splitting the routes across multiple collections in Mongo. You then need to attach the appropriate collection to each routing app.

One app for all means less admin. One app per group of forwards provides greater deployment and management flexibility.

The choice will usually depend on budget and the complexity of your environment.

Please bear in mind that production apps normally require a minimum of two dynos to provide a 24x7 SLA. And this comes with a price.

## License

Copyright © 2015 OpenGrail BVBA

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
