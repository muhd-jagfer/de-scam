'use client';

import { useState } from 'react';

type AnalyzeResponse = {
  riskLevel: string;
  score: number;
  reasons: string[];
  extractedUrl: string | null;
  domain: string | null;
  https: boolean;
};

export default function Home() {
  const [message, setMessage] = useState('');
  const [result, setResult] = useState<AnalyzeResponse | null>(null);

  const analyzeMessage = async () => {
    const response = await fetch('http://localhost:8080/api/analyze', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        message,
      }),
    });

    const data: AnalyzeResponse = await response.json();
    setResult(data);
  };

  return (
    <main className='min-h-screen bg-gray-950 text-white'>
      <div className='mx-auto max-w-4xl px-6 py-16'>
        <h1 className='text-4xl font-bold'>De-scam</h1>
        <p className='mt-2 text-gray-400'>Analyze suspicious messages before you act...</p>

        <div className='mt-10'>
          <label htmlFor='message' className='mb-2 block text-sm font-medium'>
            Message:
          </label>
          <textarea
            id='message'
            placeholder='Paste/Enter the message to analyze'
            value={message}
            onChange={(event) => setMessage(event.target.value)}
            className='h-48 w-full resize-none rounded-lg border border-gray-700 bg-gray-900 p-4 text-white outline-none focus:border-gray-500'
          />
          <button
            type='button'
            onClick={analyzeMessage}
            className='mt-4 rounded-lg bg-white px-6 py-3 font-semibold text-black hover:bg-gray-200'
          >
            Analyze
          </button>
        </div>

        {result && (
          <div className='mt-10 rounded-lg border border-gray-700 bg-gray-900 p-6'>
            <h2 className='text-xl font-semibold'>Analysis Result</h2>
            <p className='mt-4'>
              Risk: <span className='font-semibold'>{result.riskLevel}</span>
            </p>
            <p className='mt-2'>
              Score: <span className='font-semibold'>{result.score}</span>
            </p>

            <div className='mt-4'>
              <p className='font-medium'>Reasons:</p>
              <ul className='mt-2 list-disc pl-5 text-gray-300'>
                {result.reasons.map((reason, index) => (
                  <li key={index}>{reason}</li>
                ))}
              </ul>
            </div>

            {result.extractedUrl && (
              <div className='mt-4 text-gray-300'>
                <p>URL: {result.extractedUrl}</p>
                <p>DOMAIN: {result.domain ?? 'N/A'}</p>
                <p>HTTPS: {result.https ? 'yes' : 'no'}</p>
              </div>
            )}
          </div>
        )}
      </div>
    </main>
  );
}