const DID = 'did:plc:mcoftfgah4mk2ndt5qrl4sap';

export default {
  async fetch(request) {
    const { pathname } = new URL(request.url);
    if (pathname === '/.well-known/atproto-did') {
      return new Response(DID, { headers: { 'content-type': 'text/plain' } });
    }
    return Response.redirect('https://bsky.app/profile/morgan.dontsaybojio.com', 302);
  },
};
