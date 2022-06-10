import { createApi } from '@reduxjs/toolkit/query';
import { request, gql, ClientError } from 'graphql-request';

const graphqlBaseQuery =
  ({ baseUrl }: { baseUrl: string }) =>
  async ({ body }: { body: string }) => {
    try {
      const result = await request(baseUrl, body);
      return { data: result };
    } catch (error) {
      if (error instanceof ClientError) {
        return { error: { status: error.response.status, data: error } };
      }
      return { error: { status: 500, data: error } };
    }
  };

const api = createApi({
  baseQuery: graphqlBaseQuery({
    baseUrl: 'http://localhost:8080/graphql',
  }),
  endpoints: (builder) => ({
    getCount: builder.query({
      query: () => ({
        body: gql`
          query {
            count
          }
        `,
      }),
      transformResponse: (response) => response.count.count,
    }),
    incrementCount: builder.mutation({
      query: (count: number) => ({
        body: gql`
          mutation {
              setCount(count: ${count}) {
                  count
              }
          }
          `,
      }),
      transformResponse: (response) => response.setCount.count,
    }),
  }),
});

export default api;
